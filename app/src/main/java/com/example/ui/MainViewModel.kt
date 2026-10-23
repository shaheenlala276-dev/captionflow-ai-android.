package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.CaptionRepository
import com.example.data.repository.GenerationResult
import com.example.model.CaptionItem
import com.example.model.CaptionLength
import com.example.model.CaptionTone
import com.example.model.QuickCategory
import com.example.model.SocialPlatform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen {
    HOME,
    CREATE,
    SAVED
}

enum class SavedTab {
    FAVORITES,
    HISTORY
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CaptionRepository(application)

    // Navigation
    private val _currentScreen = MutableStateFlow(Screen.HOME)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Form inputs on Home
    private val _topic = MutableStateFlow("")
    val topic: StateFlow<String> = _topic.asStateFlow()

    private val _topicError = MutableStateFlow<String?>(null)
    val topicError: StateFlow<String?> = _topicError.asStateFlow()

    private val _selectedPlatform = MutableStateFlow(SocialPlatform.INSTAGRAM)
    val selectedPlatform: StateFlow<SocialPlatform> = _selectedPlatform.asStateFlow()

    private val _selectedTone = MutableStateFlow(CaptionTone.AESTHETIC)
    val selectedTone: StateFlow<CaptionTone> = _selectedTone.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Daily Life")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedLength = MutableStateFlow(CaptionLength.MEDIUM)
    val selectedLength: StateFlow<CaptionLength> = _selectedLength.asStateFlow()

    private val _selectedLanguage = MutableStateFlow("English")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    private val _includeEmojis = MutableStateFlow(true)
    val includeEmojis: StateFlow<Boolean> = _includeEmojis.asStateFlow()

    private val _includeHashtags = MutableStateFlow(true)
    val includeHashtags: StateFlow<Boolean> = _includeHashtags.asStateFlow()

    private val _variationCount = MutableStateFlow(3)
    val variationCount: StateFlow<Int> = _variationCount.asStateFlow()

    // Create Screen state
    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _currentBatch = MutableStateFlow<List<CaptionItem>>(emptyList())
    val currentBatch: StateFlow<List<CaptionItem>> = _currentBatch.asStateFlow()

    private val _generationMessage = MutableStateFlow<String?>(null)
    val generationMessage: StateFlow<String?> = _generationMessage.asStateFlow()

    private val _isLastAi = MutableStateFlow(false)
    val isLastAi: StateFlow<Boolean> = _isLastAi.asStateFlow()

    private val _editingCaption = MutableStateFlow<CaptionItem?>(null)
    val editingCaption: StateFlow<CaptionItem?> = _editingCaption.asStateFlow()

    // Saved Screen state
    private val _savedTab = MutableStateFlow(SavedTab.FAVORITES)
    val savedTab: StateFlow<SavedTab> = _savedTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filterPlatform = MutableStateFlow("All")
    val filterPlatform: StateFlow<String> = _filterPlatform.asStateFlow()

    private val _filterCategory = MutableStateFlow("All")
    val filterCategory: StateFlow<String> = _filterCategory.asStateFlow()

    private val _showClearHistoryDialog = MutableStateFlow(false)
    val showClearHistoryDialog: StateFlow<Boolean> = _showClearHistoryDialog.asStateFlow()

    // Room DB streams
    val favoritesList: StateFlow<List<CaptionItem>> = repository.favorites.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val historyList: StateFlow<List<CaptionItem>> = repository.allHistory.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // User feedback (Snackbars / toasts)
    private val _feedbackMessage = MutableStateFlow<String?>(null)
    val feedbackMessage: StateFlow<String?> = _feedbackMessage.asStateFlow()

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun onTopicChanged(newTopic: String) {
        _topic.value = newTopic
        if (newTopic.isNotBlank()) {
            _topicError.value = null
        }
    }

    fun selectPlatform(platform: SocialPlatform) {
        _selectedPlatform.value = platform
    }

    fun selectTone(tone: CaptionTone) {
        _selectedTone.value = tone
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun selectLength(length: CaptionLength) {
        _selectedLength.value = length
    }

    fun selectLanguage(language: String) {
        _selectedLanguage.value = language
    }

    fun toggleEmojis(enabled: Boolean) {
        _includeEmojis.value = enabled
    }

    fun toggleHashtags(enabled: Boolean) {
        _includeHashtags.value = enabled
    }

    fun setVariationCount(count: Int) {
        _variationCount.value = count.coerceIn(1, 5)
    }

    fun applyQuickCategory(quickCategory: QuickCategory) {
        _selectedCategory.value = quickCategory.displayName
        if (_topic.value.isBlank()) {
            _topic.value = quickCategory.samplePrompt
            _topicError.value = null
        }
    }

    fun generateCaptions() {
        val currentTopic = _topic.value.trim()
        if (currentTopic.isEmpty()) {
            _topicError.value = "Please enter a topic or photo/video description."
            return
        }
        _topicError.value = null
        _isGenerating.value = true

        // Automatically switch to Create screen
        _currentScreen.value = Screen.CREATE

        viewModelScope.launch {
            when (val result = repository.generateCaptions(
                topic = currentTopic,
                platform = _selectedPlatform.value,
                tone = _selectedTone.value,
                category = _selectedCategory.value,
                length = _selectedLength.value,
                language = _selectedLanguage.value,
                includeEmojis = _includeEmojis.value,
                includeHashtags = _includeHashtags.value,
                count = _variationCount.value
            )) {
                is GenerationResult.Success -> {
                    _currentBatch.value = result.captions
                    _isLastAi.value = result.isAiGenerated
                    _generationMessage.value = result.message
                    _isGenerating.value = false
                }
                is GenerationResult.Error -> {
                    _generationMessage.value = result.message
                    _isGenerating.value = false
                }
            }
        }
    }

    fun regenerateCaptions() {
        generateCaptions()
    }

    fun generateMoreVariations() {
        val currentTopic = _topic.value.trim()
        if (currentTopic.isEmpty()) return

        _isGenerating.value = true
        viewModelScope.launch {
            when (val result = repository.generateCaptions(
                topic = currentTopic,
                platform = _selectedPlatform.value,
                tone = _selectedTone.value,
                category = _selectedCategory.value,
                length = _selectedLength.value,
                language = _selectedLanguage.value,
                includeEmojis = _includeEmojis.value,
                includeHashtags = _includeHashtags.value,
                count = 2
            )) {
                is GenerationResult.Success -> {
                    _currentBatch.value = _currentBatch.value + result.captions
                    _generationMessage.value = "Added 2 new caption variations!"
                    _isGenerating.value = false
                }
                is GenerationResult.Error -> {
                    _isGenerating.value = false
                }
            }
        }
    }

    fun toggleFavorite(caption: CaptionItem) {
        viewModelScope.launch {
            repository.toggleFavorite(caption)
            // Update in current batch if present
            _currentBatch.value = _currentBatch.value.map {
                if (it.id == caption.id) it.copy(isFavorite = !caption.isFavorite) else it
            }
            val statusText = if (!caption.isFavorite) "Saved to favorites!" else "Removed from favorites."
            showFeedback(statusText)
        }
    }

    fun openEditDialog(caption: CaptionItem) {
        _editingCaption.value = caption
    }

    fun closeEditDialog() {
        _editingCaption.value = null
    }

    fun saveEditedCaption(newText: String) {
        val target = _editingCaption.value ?: return
        viewModelScope.launch {
            repository.updateCaptionText(target.id, newText)
            _currentBatch.value = _currentBatch.value.map {
                if (it.id == target.id) it.copy(text = newText) else it
            }
            _editingCaption.value = null
            showFeedback("Caption updated successfully!")
        }
    }

    fun deleteCaption(id: String) {
        viewModelScope.launch {
            repository.deleteCaption(id)
            _currentBatch.value = _currentBatch.value.filter { it.id != id }
            showFeedback("Caption deleted.")
        }
    }

    fun setSavedTab(tab: SavedTab) {
        _savedTab.value = tab
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun setFilterPlatform(platform: String) {
        _filterPlatform.value = platform
    }

    fun setFilterCategory(category: String) {
        _filterCategory.value = category
    }

    fun setShowClearHistoryDialog(show: Boolean) {
        _showClearHistoryDialog.value = show
    }

    fun confirmClearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
            _showClearHistoryDialog.value = false
            showFeedback("All caption history cleared.")
        }
    }

    fun showFeedback(msg: String) {
        _feedbackMessage.value = msg
    }

    fun dismissFeedback() {
        _feedbackMessage.value = null
    }
}
