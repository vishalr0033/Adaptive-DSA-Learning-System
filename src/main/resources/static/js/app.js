/**
 * AI-Based Personalized DSA Learning System - Core Client Library
 */

const App = {
    API_BASE: '/api',

    // User session helpers (stored in localStorage for persistent client interaction)
    getUser() {
        try {
            const u = localStorage.getItem('dsa_user');
            return u ? JSON.parse(u) : null;
        } catch (e) {
            return null;
        }
    },

    setUser(user) {
        localStorage.setItem('dsa_user', JSON.stringify(user));
    },

    logout() {
        localStorage.removeItem('dsa_user');
        fetch(`${this.API_BASE}/auth/logout`, { method: 'POST' }).finally(() => {
            window.location.href = '/login';
        });
    },

    requireAuth(role = 'STUDENT') {
        const user = this.getUser();
        if (!user) {
            if (role === 'ADMIN') {
                window.location.href = '/login?admin=true';
            } else {
                window.location.href = '/login';
            }
            return null;
        }
        if (role && user.role !== role) {
            if (user.role === 'ADMIN') {
                window.location.href = '/admin';
            } else {
                this.showToast('Access restricted: Admin privileges required.', 'error');
                window.location.href = '/dashboard';
            }
            return null;
        }
        return user;
    },

    // HTTP Request helper
    async request(endpoint, options = {}) {
        const url = `${this.API_BASE}${endpoint}`;
        const headers = {
            'Content-Type': 'application/json',
            ...(options.headers || {})
        };

        try {
            const resp = await fetch(url, { ...options, headers });
            const data = await resp.json();
            if (!resp.ok) {
                throw new Error(data.message || `Request failed (${resp.status})`);
            }
            return data;
        } catch (err) {
            console.error('API Error:', err);
            throw err;
        }
    },

    // UI Toast Notification
    showToast(message, type = 'info') {
        let container = document.getElementById('toast-container');
        if (!container) {
            container = document.createElement('div');
            container.id = 'toast-container';
            container.className = 'toast-container';
            document.body.appendChild(container);
        }

        const toast = document.createElement('div');
        toast.className = `toast ${type}`;
        
        let icon = 'ℹ️';
        if (type === 'success') icon = '✅';
        if (type === 'error') icon = '⚠️';

        toast.innerHTML = `<span>${icon}</span><span>${message}</span>`;
        container.appendChild(toast);

        setTimeout(() => {
            toast.style.opacity = '0';
            toast.style.transform = 'translateX(100%)';
            toast.style.transition = 'all 0.3s ease';
            setTimeout(() => toast.remove(), 300);
        }, 4000);
    },

    // Render Navigation Header
    renderNav(activeTab = 'dashboard') {
        const user = this.getUser();
        const header = document.querySelector('.app-header');
        if (!header) return;

        if (!user) {
            header.innerHTML = `
                <div class="header-container">
                    <a href="/" class="brand-logo">
                        <div class="brand-icon">&lt;/&gt;</div>
                        <span>Adaptive DSA Learning</span>
                        <span class="brand-badge">AI Powered</span>
                    </a>
                    <div style="display:flex;gap:0.75rem;">
                        <a href="/login" class="btn btn-secondary btn-sm">Login</a>
                        <a href="/register" class="btn btn-primary btn-sm">Register</a>
                    </div>
                </div>
            `;
            return;
        }

        if (user.role === 'ADMIN') {
            header.innerHTML = `
                <div class="header-container">
                    <a href="/admin" class="brand-logo">
                        <div class="brand-icon">⚙️</div>
                        <span>DSA Admin Panel</span>
                        <span class="brand-badge">Master Control</span>
                    </a>
                    <div style="display:flex;align-items:center;gap:1rem;">
                        <span style="font-size:0.85rem;color:var(--text-secondary)">Logged in as <b>${user.name}</b></span>
                        <button onclick="App.logout()" class="btn btn-secondary btn-sm">Logout</button>
                    </div>
                </div>
            `;
            return;
        }

        // Student Nav
        header.innerHTML = `
            <div class="header-container">
                <a href="/dashboard" class="brand-logo">
                    <div class="brand-icon">&lt;/&gt;</div>
                    <span>Adaptive DSA Learning</span>
                    <span class="brand-badge">AI Powered</span>
                </a>
                <ul class="nav-links">
                    <li><a href="/dashboard" class="nav-link ${activeTab === 'dashboard' ? 'active' : ''}">📊 Dashboard</a></li>
                    <li><a href="/problems" class="nav-link ${activeTab === 'problems' ? 'active' : ''}">💻 Problems</a></li>
                    <li><a href="/analysis" class="nav-link ${activeTab === 'analysis' ? 'active' : ''}">🧠 My Coding Analysis</a></li>
                    <li><a href="/recommendations" class="nav-link ${activeTab === 'recommendations' ? 'active' : ''}">🎯 Recommendations</a></li>
                    <li><a href="/improvement" class="nav-link ${activeTab === 'improvement' ? 'active' : ''}">📈 Improvement</a></li>
                    <li><a href="/submissions" class="nav-link ${activeTab === 'submissions' ? 'active' : ''}">📜 Submissions</a></li>
                </ul>
                <div class="user-profile-pill">
                    <div class="user-avatar">${user.name ? user.name.charAt(0).toUpperCase() : 'S'}</div>
                    <span class="user-name">${user.name ? user.name.split(' ')[0] : 'Student'}</span>
                    <button onclick="App.logout()" class="btn-logout" title="Logout">🚪</button>
                </div>
            </div>
        `;
    }
};
