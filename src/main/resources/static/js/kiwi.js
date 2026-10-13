/**
 * Kiwi 🥝 - AI DSA Mentor & Coding Assistant Widget
 */
const KiwiBot = {
    isOpen: false,
    history: [],
    problemId: null,

    init(options = {}) {
        if (options.problemId) {
            this.problemId = options.problemId;
        }

        // Ensure stylesheet is loaded
        if (!document.getElementById('kiwi-css')) {
            const link = document.createElement('link');
            link.id = 'kiwi-css';
            link.rel = 'stylesheet';
            link.href = '/css/kiwi.css';
            document.head.appendChild(link);
        }

        // Render Widget HTML
        this.renderWidget();
        this.attachEvents();
    },

    setProblemId(id) {
        this.problemId = id;
    },

    renderWidget() {
        if (document.getElementById('kiwi-widget-root')) return;

        const container = document.createElement('div');
        container.id = 'kiwi-widget-root';
        container.innerHTML = `
            <!-- Floating Bubble Trigger -->
            <button id="kiwi-toggle-btn" class="kiwi-floating-btn" title="Ask Kiwi 🥝 (AI DSA Mentor)">
                <span class="kiwi-avatar-icon">🥝</span>
                <span>Ask Kiwi</span>
                <span class="kiwi-pulse-dot"></span>
            </button>

            <!-- Chat Drawer / Window -->
            <div id="kiwi-chat-box" class="kiwi-chat-window" style="display:none;">
                <div class="kiwi-header">
                    <div class="kiwi-header-left">
                        <div class="kiwi-avatar-icon" style="font-size:1.5rem;">🥝</div>
                        <div>
                            <div class="kiwi-header-title">Kiwi AI <span style="font-size:0.75rem;background:rgba(16,185,129,0.2);color:#34d399;padding:1px 6px;border-radius:10px;">DSA Mentor</span></div>
                            <div class="kiwi-header-subtitle">● Online & Ready to Help</div>
                        </div>
                    </div>
                    <div class="kiwi-header-actions">
                        <button id="kiwi-clear-btn" class="kiwi-icon-btn" title="Clear Chat">🧹</button>
                        <button id="kiwi-close-btn" class="kiwi-icon-btn" title="Close">✕</button>
                    </div>
                </div>

                <div id="kiwi-messages-list" class="kiwi-messages">
                    <!-- Initial Welcome Message -->
                    <div class="kiwi-msg bot">
                        <div class="kiwi-msg-avatar">🥝</div>
                        <div class="kiwi-msg-content">
                            <p>👋 Hey there! I'm <strong>Kiwi 🥝</strong>, your personal AI DSA Mentor.</p>
                            <p>Stuck on an error, need a hint without spoiling the answer, or curious about time complexity? Click a topic below or ask me anything!</p>
                        </div>
                    </div>
                </div>

                <!-- Quick Action Pills -->
                <div id="kiwi-pills-bar" class="kiwi-quick-pills">
                    <button class="kiwi-pill" onclick="KiwiBot.sendQuick('💡 Give me a hint for this problem')">💡 Get a Hint</button>
                    <button class="kiwi-pill" onclick="KiwiBot.sendQuick('🔍 Why is my code failing?')">🔍 Explain Mistake</button>
                    <button class="kiwi-pill" onclick="KiwiBot.sendQuick('⚡ Check my code time complexity')">⚡ Complexity</button>
                    <button class="kiwi-pill" onclick="KiwiBot.sendQuick('🎯 What approaches are supported?')">🎯 Approaches</button>
                </div>

                <!-- Input Field -->
                <form id="kiwi-input-form" class="kiwi-input-bar">
                    <textarea id="kiwi-user-input" class="kiwi-input" placeholder="Ask Kiwi a DSA question..." rows="1"></textarea>
                    <button type="submit" id="kiwi-send-btn" class="kiwi-send-btn" title="Send message">➤</button>
                </form>
            </div>
        `;
        document.body.appendChild(container);
    },

    attachEvents() {
        const toggleBtn = document.getElementById('kiwi-toggle-btn');
        const closeBtn = document.getElementById('kiwi-close-btn');
        const clearBtn = document.getElementById('kiwi-clear-btn');
        const form = document.getElementById('kiwi-input-form');
        const input = document.getElementById('kiwi-user-input');

        toggleBtn.addEventListener('click', () => this.toggleChat());
        closeBtn.addEventListener('click', () => this.toggleChat(false));
        clearBtn.addEventListener('click', () => this.clearChat());

        // Auto-expand textarea & Enter to submit (Shift+Enter for new line)
        input.addEventListener('keydown', (e) => {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                form.dispatchEvent(new Event('submit'));
            }
        });

        form.addEventListener('submit', (e) => {
            e.preventDefault();
            const text = input.value.trim();
            if (!text) return;
            input.value = '';
            this.sendMessage(text);
        });
    },

    toggleChat(forceState = null) {
        this.isOpen = (forceState !== null) ? forceState : !this.isOpen;
        const box = document.getElementById('kiwi-chat-box');
        if (box) {
            box.style.display = this.isOpen ? 'flex' : 'none';
            if (this.isOpen) {
                setTimeout(() => document.getElementById('kiwi-user-input')?.focus(), 100);
            }
        }
    },

    clearChat() {
        const list = document.getElementById('kiwi-messages-list');
        if (list) {
            list.innerHTML = `
                <div class="kiwi-msg bot">
                    <div class="kiwi-msg-avatar">🥝</div>
                    <div class="kiwi-msg-content">
                        <p>Chat cleared! 🧹 What DSA question can I help you with now?</p>
                    </div>
                </div>
            `;
        }
    },

    sendQuick(text) {
        if (!this.isOpen) this.toggleChat(true);
        this.sendMessage(text);
    },

    async sendMessage(userText) {
        const messagesList = document.getElementById('kiwi-messages-list');
        const sendBtn = document.getElementById('kiwi-send-btn');
        const pillsBar = document.getElementById('kiwi-pills-bar');

        // Append User Message
        this.appendMessage('user', userText);

        // Append Typing Indicator
        const typingId = 'kiwi-typing-' + Date.now();
        const typingElem = document.createElement('div');
        typingElem.id = typingId;
        typingElem.className = 'kiwi-msg bot';
        typingElem.innerHTML = `
            <div class="kiwi-msg-avatar">🥝</div>
            <div class="kiwi-msg-content">
                <div class="kiwi-typing">
                    <div class="kiwi-dot"></div>
                    <div class="kiwi-dot"></div>
                    <div class="kiwi-dot"></div>
                </div>
            </div>
        `;
        messagesList.appendChild(typingElem);
        messagesList.scrollTop = messagesList.scrollHeight;
        if (sendBtn) sendBtn.disabled = true;

        // Extract active context from editor if present
        let currentCode = '';
        let lastError = '';
        let problemId = this.problemId;

        // Try getting from window.CodeEditor or DOM
        if (window.editorInstance && typeof window.editorInstance.getValue === 'function') {
            currentCode = window.editorInstance.getValue();
        } else if (document.getElementById('code-editor')) {
            currentCode = document.getElementById('code-editor').value || '';
        }

        // Try getting last error from editor console/output
        const consoleOut = document.getElementById('console-output') || document.getElementById('test-results-container');
        if (consoleOut) {
            lastError = consoleOut.innerText.trim();
        }

        const student = (typeof App !== 'undefined' && App.getStudent) ? App.getStudent() : null;

        const payload = {
            message: userText,
            problemId: problemId,
            currentCode: currentCode,
            lastError: lastError,
            studentId: student ? student.id : null
        };

        try {
            const res = await fetch('/api/kiwi/chat', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            const data = await res.json();
            
            // Remove typing indicator
            const t = document.getElementById(typingId);
            if (t) t.remove();

            if (data && data.reply) {
                this.appendMessage('bot', data.reply);
                
                // Update dynamic suggestion pills if provided
                if (data.suggestedQuestions && data.suggestedQuestions.length > 0 && pillsBar) {
                    pillsBar.innerHTML = data.suggestedQuestions.map(q => 
                        `<button class="kiwi-pill" onclick="KiwiBot.sendQuick('${q.replace(/'/g, "\\'")}')">${q}</button>`
                    ).join('');
                }
            } else {
                this.appendMessage('bot', "I'm having a little trouble connecting right now, but feel free to ask me again! 🥝");
            }
        } catch (err) {
            const t = document.getElementById(typingId);
            if (t) t.remove();
            this.appendMessage('bot', "⚠️ Connection issue: " + err.message + ". Please try again!");
        } finally {
            if (sendBtn) sendBtn.disabled = false;
        }
    },

    appendMessage(sender, text) {
        const list = document.getElementById('kiwi-messages-list');
        if (!list) return;

        const msgDiv = document.createElement('div');
        msgDiv.className = `kiwi-msg ${sender}`;

        const formattedHtml = this.formatMarkdown(text);

        if (sender === 'user') {
            msgDiv.innerHTML = `<div class="kiwi-msg-content">${formattedHtml}</div>`;
        } else {
            msgDiv.innerHTML = `
                <div class="kiwi-msg-avatar">🥝</div>
                <div class="kiwi-msg-content">${formattedHtml}</div>
            `;
        }

        list.appendChild(msgDiv);
        list.scrollTop = list.scrollHeight;
    },

    formatMarkdown(text) {
        if (!text) return '';
        
        let html = text
            // Escape special HTML
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;');

        // Code blocks ```java ... ```
        html = html.replace(/```(?:[a-zA-Z]*)\n([\s\S]*?)```/g, '<pre><code>$1</code></pre>');

        // Inline code `...`
        html = html.replace(/`([^`]+)`/g, '<code>$1</code>');

        // Headers ###
        html = html.replace(/^### (.*$)/gim, '<h3>$1</h3>');
        html = html.replace(/^## (.*$)/gim, '<h3>$1</h3>');

        // Bold **...**
        html = html.replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>');

        // Bullet lists
        html = html.replace(/^\s*-\s+(.*$)/gim, '<li>$1</li>');
        html = html.replace(/(<li>.*<\/li>)/gims, '<ul>$1</ul>');

        // Line breaks into paragraphs
        const paragraphs = html.split(/\n\n+/);
        html = paragraphs.map(p => {
            if (p.startsWith('<h3>') || p.startsWith('<pre>') || p.startsWith('<ul>')) return p;
            return `<p>${p.replace(/\n/g, '<br>')}</p>`;
        }).join('');

        return html;
    }
};

// Auto-initialize when loaded on any page
document.addEventListener('DOMContentLoaded', () => {
    // Check if on editor page to grab ID from query params
    const urlParams = new URLSearchParams(window.location.search);
    const pId = urlParams.get('id');
    KiwiBot.init({ problemId: pId ? parseInt(pId) : null });
});
