/**
 * Interactive Code Studio & Test Runner Controller
 */

const CodeStudio = {
    problem: null,
    sampleTestCases: [],
    activeBottomTab: 'testcases',
    isBottomPanelCollapsed: false,

    async init(problemId) {
        try {
            const problem = await App.request(`/problems/${problemId}`);
            this.problem = problem;

            // Fetch sample test cases
            this.sampleTestCases = await App.request(`/problems/${problemId}/testcases`);

            this.renderProblemStatement();
            this.setupCodeEditor();
            this.renderTestCaseTabs();
            this.switchBottomTab('testcases');
        } catch (err) {
            App.showToast('Failed to load problem: ' + err.message, 'error');
        }
    },

    renderProblemStatement() {
        if (!this.problem) return;
        document.getElementById('problem-title').innerText = `${this.problem.id}. ${this.problem.title}`;
        document.title = `${this.problem.id}. ${this.problem.title} - Code Studio`;

        // Difficulty & Topic badges
        const diffEl = document.getElementById('problem-difficulty');
        diffEl.className = `badge badge-${this.problem.difficulty.toLowerCase()}`;
        diffEl.innerText = this.problem.difficulty;

        const topicEl = document.getElementById('problem-topic');
        topicEl.className = 'badge badge-topic';
        topicEl.innerText = this.problem.topic ? this.problem.topic.replace('_', ' ') : 'General';

        const altContainer = document.getElementById('problem-alt-topics');
        if (altContainer) {
            if (this.problem.alternativeTopics && this.problem.alternativeTopics.trim().length > 0) {
                altContainer.innerHTML = this.problem.alternativeTopics.split(',')
                    .map(t => `<span class="badge" style="background:rgba(255,255,255,0.08);color:#94a3b8;font-size:0.72rem;padding:0.2rem 0.4rem;">${t.trim().replace('_', ' ')}</span>`)
                    .join('');
            } else {
                altContainer.innerHTML = '';
            }
        }

        const appBox = document.getElementById('problem-approaches-box');
        if (appBox) {
            if (this.problem.supportedApproaches && this.problem.supportedApproaches.trim().length > 0) {
                appBox.style.display = 'block';
                appBox.innerHTML = `<b>💡 Supported Approaches:</b> ${this.problem.supportedApproaches}`;
            } else {
                appBox.style.display = 'none';
            }
        }

        document.getElementById('problem-desc').innerHTML = this.formatDescription(this.problem.description);
        document.getElementById('example-input').innerText = this.problem.exampleInput || '';
        document.getElementById('example-output').innerText = this.problem.exampleOutput || '';
        document.getElementById('problem-constraints').innerHTML = (this.problem.constraints || '')
            .split('\n')
            .map(c => `<li><code>${c}</code></li>`)
            .join('');
    },

    formatDescription(text) {
        if (!text) return '';
        return text.replace(/`([^`]+)`/g, '<code>$1</code>').replace(/\n/g, '<br>');
    },

    setupCodeEditor() {
        const textarea = document.getElementById('code-editor');
        if (!textarea) return;

        // Set starter code
        textarea.value = this.problem.starterCode || 'class Solution {\n    public int solve(int[] nums) {\n        // Write code\n    }\n}';

        // Advanced IDE Indentation & Keyboard Engine
        textarea.addEventListener('keydown', function(e) {
            const start = this.selectionStart;
            const end = this.selectionEnd;
            const value = this.value;

            // 1. Tab and Shift+Tab (Indentation & Block Indent)
            if (e.key === 'Tab') {
                e.preventDefault();
                const isShift = e.shiftKey;

                // Multi-line selection indent/unindent
                if (start !== end && value.substring(start, end).includes('\n')) {
                    const lineStart = value.lastIndexOf('\n', start - 1) + 1;
                    const lineEnd = value.indexOf('\n', end);
                    const effectiveEnd = lineEnd === -1 ? value.length : lineEnd;

                    const selectedBlock = value.substring(lineStart, effectiveEnd);
                    const lines = selectedBlock.split('\n');

                    let modifiedLines;
                    if (isShift) {
                        // Unindent 4 spaces
                        modifiedLines = lines.map(line => line.startsWith('    ') ? line.substring(4) : line.replace(/^\t/, ''));
                    } else {
                        // Indent 4 spaces
                        modifiedLines = lines.map(line => '    ' + line);
                    }

                    const newBlock = modifiedLines.join('\n');
                    this.value = value.substring(0, lineStart) + newBlock + value.substring(effectiveEnd);
                    this.selectionStart = lineStart;
                    this.selectionEnd = lineStart + newBlock.length;
                } else {
                    // Single cursor or single line
                    if (isShift) {
                        const lineStart = value.lastIndexOf('\n', start - 1) + 1;
                        if (value.substring(lineStart, lineStart + 4) === '    ') {
                            this.value = value.substring(0, lineStart) + value.substring(lineStart + 4);
                            this.selectionStart = this.selectionEnd = Math.max(lineStart, start - 4);
                        }
                    } else {
                        // Insert 4 spaces at cursor
                        this.value = value.substring(0, start) + "    " + value.substring(end);
                        this.selectionStart = this.selectionEnd = start + 4;
                    }
                }
            }

            // 2. Enter Key (Auto-Indentation)
            else if (e.key === 'Enter') {
                e.preventDefault();
                const lastNewline = value.lastIndexOf('\n', start - 1);
                const currentLine = value.substring(lastNewline + 1, start);
                const match = currentLine.match(/^(\s*)/);
                const indent = match ? match[1] : '';

                const charBefore = value[start - 1];
                const charAfter = value[start];

                // Between curly braces: { | }
                if (charBefore === '{' && charAfter === '}') {
                    const nextIndent = indent + '    ';
                    const insertion = '\n' + nextIndent + '\n' + indent;
                    this.value = value.substring(0, start) + insertion + value.substring(end);
                    this.selectionStart = this.selectionEnd = start + nextIndent.length + 1;
                } else if (charBefore === '{' || charBefore === '(' || currentLine.trim().endsWith('{')) {
                    const nextIndent = indent + '    ';
                    this.value = value.substring(0, start) + '\n' + nextIndent + value.substring(end);
                    this.selectionStart = this.selectionEnd = start + nextIndent.length + 1;
                } else {
                    this.value = value.substring(0, start) + '\n' + indent + value.substring(end);
                    this.selectionStart = this.selectionEnd = start + indent.length + 1;
                }
            }

            // 3. Auto-Closing Pairs
            const pairs = { '(': ')', '[': ']', '{': '}', '"': '"', "'": "'" };
            if (pairs[e.key]) {
                const closeChar = pairs[e.key];
                // If character right ahead is same quote, step over
                if ((e.key === '"' || e.key === "'") && value[start] === e.key && start === end) {
                    e.preventDefault();
                    this.selectionStart = this.selectionEnd = start + 1;
                    return;
                }

                e.preventDefault();
                const selectedText = value.substring(start, end);
                this.value = value.substring(0, start) + e.key + selectedText + closeChar + value.substring(end);
                this.selectionStart = this.selectionEnd = start + 1;
            }

            // Step over closing brackets: ), ], }
            else if ((e.key === ')' || e.key === ']' || e.key === '}') && value[start] === e.key && start === end) {
                e.preventDefault();
                this.selectionStart = this.selectionEnd = start + 1;
            }

            // 4. Backspace Pair Deletion
            else if (e.key === 'Backspace' && start === end && start > 0) {
                const prev = value[start - 1];
                const next = value[start];
                if ((prev === '(' && next === ')') ||
                    (prev === '[' && next === ']') ||
                    (prev === '{' && next === '}') ||
                    (prev === '"' && next === '"') ||
                    (prev === "'" && next === "'")) {
                    e.preventDefault();
                    this.value = value.substring(0, start - 1) + value.substring(start + 1);
                    this.selectionStart = this.selectionEnd = start - 1;
                }
            }
        });
    },

    switchBottomTab(tab) {
        this.activeBottomTab = tab;
        const testcaseBtn = document.getElementById('tab-btn-testcases');
        const resultsBtn = document.getElementById('tab-btn-results');
        const testcaseContent = document.getElementById('testcase-tab-content');
        const outputDrawer = document.getElementById('output-drawer');

        // Uncollapse if collapsed
        if (this.isBottomPanelCollapsed) {
            this.toggleBottomPanel(false);
        }

        if (tab === 'testcases') {
            if (testcaseBtn) {
                testcaseBtn.style.background = 'rgba(255,255,255,0.14)';
                testcaseBtn.style.color = '#fff';
            }
            if (resultsBtn) {
                resultsBtn.style.background = 'transparent';
                resultsBtn.style.color = 'var(--text-muted)';
            }
            if (testcaseContent) testcaseContent.style.display = 'flex';
            if (outputDrawer) outputDrawer.style.display = 'none';
        } else {
            if (resultsBtn) {
                resultsBtn.style.background = 'rgba(255,255,255,0.14)';
                resultsBtn.style.color = '#fff';
            }
            if (testcaseBtn) {
                testcaseBtn.style.background = 'transparent';
                testcaseBtn.style.color = 'var(--text-muted)';
            }
            if (testcaseContent) testcaseContent.style.display = 'none';
            if (outputDrawer) outputDrawer.style.display = 'block';
        }
    },

    toggleBottomPanel(forceState = null) {
        const container = document.getElementById('bottom-panel-container');
        const toggleBtn = document.getElementById('btn-toggle-bottom');
        const testcaseContent = document.getElementById('testcase-tab-content');
        const outputDrawer = document.getElementById('output-drawer');

        this.isBottomPanelCollapsed = (forceState !== null) ? forceState : !this.isBottomPanelCollapsed;

        if (this.isBottomPanelCollapsed) {
            if (container) container.style.maxHeight = '36px';
            if (testcaseContent) testcaseContent.style.display = 'none';
            if (outputDrawer) outputDrawer.style.display = 'none';
            if (toggleBtn) toggleBtn.innerText = '⌃ Expand';
        } else {
            if (container) container.style.maxHeight = '270px';
            if (toggleBtn) toggleBtn.innerText = '⌄ Collapse';
            // Restore active tab
            if (this.activeBottomTab === 'testcases') {
                if (testcaseContent) testcaseContent.style.display = 'flex';
                if (outputDrawer) outputDrawer.style.display = 'none';
            } else {
                if (testcaseContent) testcaseContent.style.display = 'none';
                if (outputDrawer) outputDrawer.style.display = 'block';
            }
        }
    },

    resetCode() {
        if (confirm('Reset editor to initial starter template?')) {
            const textarea = document.getElementById('code-editor');
            if (textarea && this.problem) {
                textarea.value = this.problem.starterCode;
            }
        }
    },

    renderTestCaseTabs() {
        const container = document.getElementById('testcase-tabs');
        if (!container) return;

        let html = '';
        this.sampleTestCases.forEach((tc, idx) => {
            html += `
                <button class="btn btn-secondary btn-sm tc-tab-btn ${idx === 0 ? 'active' : ''}" 
                        style="font-size:0.75rem;padding:0.2rem 0.55rem;border-radius:4px;"
                        onclick="CodeStudio.selectTestCaseTab(${idx})">
                    Case ${idx + 1}
                </button>
            `;
        });
        container.innerHTML = html;
        if (this.sampleTestCases.length > 0) {
            this.displayTestCaseDetails(0);
        }
    },

    selectTestCaseTab(index) {
        document.querySelectorAll('.tc-tab-btn').forEach((btn, idx) => {
            if (idx === index) {
                btn.classList.add('active');
                btn.style.background = 'var(--bg-tertiary)';
                btn.style.color = '#fff';
            } else {
                btn.classList.remove('active');
                btn.style.background = 'transparent';
                btn.style.color = 'var(--text-muted)';
            }
        });
        this.displayTestCaseDetails(index);
    },

    displayTestCaseDetails(index) {
        const tc = this.sampleTestCases[index];
        if (!tc) return;

        const body = document.getElementById('testcase-body');
        if (body) {
            body.innerHTML = `
                <div style="margin-bottom:0.5rem;">
                    <div style="font-size:0.75rem;color:var(--text-muted);margin-bottom:0.2rem;font-weight:600;">INPUT:</div>
                    <pre style="background:#040711;padding:0.5rem 0.75rem;border-radius:4px;color:#cbd5e1;font-family:var(--font-mono);font-size:0.82rem;margin:0;">${tc.input}</pre>
                </div>
                <div>
                    <div style="font-size:0.75rem;color:var(--text-muted);margin-bottom:0.2rem;font-weight:600;">EXPECTED OUTPUT:</div>
                    <pre style="background:#040711;padding:0.5rem 0.75rem;border-radius:4px;color:#10b981;font-family:var(--font-mono);font-size:0.82rem;margin:0;">${tc.expectedOutput}</pre>
                </div>
            `;
        }
    },

    async runCode() {
        const user = App.getUser();
        if (!user) {
            App.showToast('Please log in to run code', 'error');
            return;
        }

        const code = document.getElementById('code-editor').value;
        const btn = document.getElementById('btn-run');
        btn.disabled = true;
        btn.innerHTML = '⏳ Running...';

        this.switchBottomTab('results');
        const outputDrawer = document.getElementById('output-drawer');
        outputDrawer.innerHTML = '<div style="color:var(--accent-cyan);padding:0.5rem 0;">⚙️ Compiling and executing against sample test cases...</div>';

        try {
            const res = await App.request('/code/run', {
                method: 'POST',
                body: JSON.stringify({
                    problemId: this.problem.id,
                    code: code
                })
            });

            this.renderExecutionOutput(res, false);
        } catch (err) {
            outputDrawer.innerHTML = `<div style="color:var(--status-wrong);padding:0.5rem 0;">Execution failed: ${err.message}</div>`;
        } finally {
            btn.disabled = false;
            btn.innerHTML = '▶ Run Code';
        }
    },

    async submitCode() {
        const user = App.getUser();
        if (!user) {
            App.showToast('Please log in to submit code', 'error');
            return;
        }

        const code = document.getElementById('code-editor').value;
        const btn = document.getElementById('btn-submit');
        btn.disabled = true;
        btn.innerHTML = '🚀 Submitting...';

        this.switchBottomTab('results');
        const outputDrawer = document.getElementById('output-drawer');
        outputDrawer.innerHTML = '<div style="color:var(--accent-cyan);padding:0.5rem 0;">🚀 Executing full test suite and generating AI analysis...</div>';

        try {
            const res = await App.request('/code/submit', {
                method: 'POST',
                body: JSON.stringify({
                    studentId: user.id,
                    problemId: this.problem.id,
                    code: code
                })
            });

            this.renderExecutionOutput(res, true);
        } catch (err) {
            outputDrawer.innerHTML = `<div style="color:var(--status-wrong);padding:0.5rem 0;">Submission failed: ${err.message}</div>`;
        } finally {
            btn.disabled = false;
            btn.innerHTML = '🚀 Submit Code';
        }
    },

    renderExecutionOutput(res, isSubmission) {
        const drawer = document.getElementById('output-drawer');
        const badgeSpan = document.getElementById('results-badge');
        if (!drawer) return;

        let statusBadge = '';
        let badgeColor = '#10b981';
        let badgeText = 'Accepted';

        if (res.status === 'ACCEPTED') {
            statusBadge = '<span class="badge badge-accepted" style="font-size:0.85rem;padding:0.3rem 0.65rem;">🎉 Accepted</span>';
            badgeText = 'Passed';
            badgeColor = '#10b981';
        } else if (res.status === 'SUBOPTIMAL_APPROACH') {
            statusBadge = '<span class="badge badge-suboptimal" style="font-size:0.85rem;padding:0.3rem 0.65rem;">⚠️ Sub-optimal Approach</span>';
            badgeText = 'Sub-optimal';
            badgeColor = '#f59e0b';
        } else if (res.status === 'RESTRICTION_ERROR') {
            statusBadge = '<span class="badge badge-restriction" style="font-size:0.85rem;padding:0.3rem 0.65rem;">⛔ Topic Restriction</span>';
            badgeText = 'Restriction';
            badgeColor = '#ef4444';
        } else if (res.status === 'WRONG_ANSWER') {
            statusBadge = '<span class="badge badge-wrong" style="font-size:0.85rem;padding:0.3rem 0.65rem;">❌ Wrong Answer</span>';
            badgeText = 'Failed';
            badgeColor = '#ef4444';
        } else if (res.status === 'COMPILATION_ERROR') {
            statusBadge = '<span class="badge badge-compile" style="font-size:0.85rem;padding:0.3rem 0.65rem;">⚠️ Compilation Error</span>';
            badgeText = 'Error';
            badgeColor = '#f59e0b';
        } else {
            statusBadge = `<span class="badge badge-tle" style="font-size:0.85rem;padding:0.3rem 0.65rem;">⏱️ ${res.statusText || res.status}</span>`;
            badgeText = 'TLE';
            badgeColor = '#f97316';
        }

        if (badgeSpan) {
            badgeSpan.style.display = 'inline-block';
            badgeSpan.style.background = badgeColor;
            badgeSpan.style.color = '#fff';
            badgeSpan.style.fontSize = '0.68rem';
            badgeSpan.style.padding = '1px 5px';
            badgeSpan.style.borderRadius = '8px';
            badgeSpan.innerText = badgeText;
        }

        let html = `
            <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:0.75rem;">
                <div style="display:flex;align-items:center;gap:0.6rem;">
                    ${statusBadge}
                    ${res.status === 'SUBOPTIMAL_APPROACH' ? `
                        <span style="font-size:0.8rem;color:#fbbf24;font-weight:600;">
                            ${res.passedCount} / ${res.totalCount} Cases Passed — Needs Optimal Pattern
                        </span>
                    ` : (res.status !== 'RESTRICTION_ERROR' ? `
                        <span style="font-size:0.8rem;color:var(--text-secondary);">
                            ${res.passedCount} / ${res.totalCount} Cases Passed
                        </span>
                    ` : `
                        <span style="font-size:0.8rem;color:#f87171;font-weight:600;">
                            Must use required DSA Topic approach
                        </span>
                    `)}
                </div>
                <div style="font-size:0.78rem;color:var(--text-muted);">
                    Runtime: <b>${res.executionTimeMs || 0} ms</b>
                </div>
            </div>
        `;

        if (res.status === 'SUBOPTIMAL_APPROACH') {
            html += `
                <div style="background:rgba(245,158,11,0.12);border:1px solid #f59e0b;padding:0.75rem;border-radius:6px;margin-bottom:0.75rem;color:#fef3c7;">
                    <div style="font-weight:800;font-size:0.88rem;margin-bottom:0.3rem;display:flex;align-items:center;gap:0.4rem;color:#fbbf24;">
                        <span>🧠</span> AI COGNITIVE THOUGHT PROCESS & TRANSITION DIAGNOSIS
                    </div>
                    <div style="white-space:pre-wrap;font-family:var(--font-sans);font-size:0.82rem;color:#fef3c7;line-height:1.5;">${res.explanation || res.compilerError}</div>
                </div>
            `;
        } else if (res.status === 'RESTRICTION_ERROR') {
            html += `
                <div style="background:rgba(239,68,68,0.12);border:1px solid #ef4444;padding:0.75rem;border-radius:6px;margin-bottom:0.75rem;color:#fca5a5;">
                    <div style="font-weight:800;font-size:0.88rem;margin-bottom:0.3rem;display:flex;align-items:center;gap:0.4rem;">
                        <span>🚫</span> DSA TOPIC RESTRICTION VIOLATION
                    </div>
                    <pre style="white-space:pre-wrap;font-family:var(--font-sans);font-size:0.82rem;color:#fee2e2;line-height:1.4;margin:0;">${res.compilerError}</pre>
                </div>
            `;
        } else if (res.compilerError) {
            html += `
                <div style="background:rgba(245,158,11,0.1);border:1px solid rgba(245,158,11,0.3);padding:0.65rem;border-radius:6px;margin-bottom:0.75rem;color:#fbbf24;">
                    <div style="font-weight:700;font-size:0.8rem;margin-bottom:0.25rem;">Output / Diagnostic Message:</div>
                    <pre style="white-space:pre-wrap;font-family:var(--font-mono);font-size:0.8rem;margin:0;">${res.compilerError}</pre>
                </div>
            `;
        }

        // Test Cases Breakdown
        if (res.testCaseResults && res.testCaseResults.length > 0) {
            html += '<div style="margin-top:0.5rem;"><div style="font-weight:700;font-size:0.82rem;margin-bottom:0.4rem;color:var(--text-secondary);">Test Case Breakdown:</div>';
            res.testCaseResults.forEach(tc => {
                const passClass = tc.passed ? 'var(--status-accepted)' : 'var(--status-wrong)';
                const icon = tc.passed ? '✅' : '❌';
                html += `
                    <div style="background:var(--bg-secondary);padding:0.5rem 0.75rem;border-radius:6px;margin-bottom:0.4rem;border-left:3px solid ${passClass};">
                        <div style="display:flex;justify-content:space-between;font-size:0.8rem;">
                            <span><b>Test Case ${tc.testIndex}</b> ${tc.sample ? '(Sample)' : '(Hidden)'}</span>
                            <span style="color:${passClass};font-weight:700;">${icon} ${tc.passed ? 'Passed' : 'Failed'}</span>
                        </div>
                        ${!tc.passed ? `
                            <div style="margin-top:0.3rem;font-size:0.76rem;color:var(--text-muted);line-height:1.4;">
                                <div>Input: <code>${tc.input}</code></div>
                                <div>Expected: <code style="color:var(--status-accepted);">${tc.expectedOutput}</code></div>
                                <div>Actual: <code style="color:var(--status-wrong);">${tc.actualOutput || 'None'}</code></div>
                            </div>
                        ` : ''}
                    </div>
                `;
            });
            html += '</div>';
        }

        // AI Mistake Diagnosis Box
        if (res.hasMistakeAnalysis) {
            html += `
                <div style="margin-top:0.75rem;background:linear-gradient(135deg, rgba(30,41,59,0.95), rgba(139,92,246,0.15));border:1px solid rgba(139,92,246,0.4);border-radius:8px;padding:0.85rem;">
                    <div style="display:flex;align-items:center;justify-content:space-between;margin-bottom:0.5rem;">
                        <div style="display:flex;align-items:center;gap:0.4rem;">
                            <span style="font-size:1.1rem;">🤖</span>
                            <span style="font-weight:700;font-size:0.88rem;color:var(--accent-purple);">AI Mistake Diagnosis</span>
                            <span class="badge" style="background:rgba(139,92,246,0.2);color:var(--accent-purple);font-size:0.72rem;">${res.mistakeCategory}</span>
                        </div>
                        ${res.recurring ? `
                            <span class="badge badge-wrong" style="font-size:0.7rem;" title="Recurring mistake detected">
                                🔄 Repeated Mistake (${res.recurringCount}x)
                            </span>
                        ` : ''}
                    </div>

                    <div style="font-size:0.88rem;font-weight:600;color:#fff;margin-bottom:0.25rem;">
                        ${res.specificMistake}
                    </div>
                    
                    <div style="font-size:0.82rem;color:var(--text-secondary);margin-bottom:0.5rem;line-height:1.4;">
                        <b>Why you are struggling:</b> ${res.explanation}
                    </div>

                    ${res.rootCause ? `
                        <div style="font-size:0.8rem;color:var(--text-muted);margin-bottom:0.4rem;">
                            <b>Root Cause in Code:</b> <code>${res.rootCause}</code>
                        </div>
                    ` : ''}

                    ${res.suggestedFix ? `
                        <div style="background:rgba(16,185,129,0.1);border:1px solid rgba(16,185,129,0.3);padding:0.5rem 0.75rem;border-radius:6px;font-size:0.8rem;color:#34d399;line-height:1.4;">
                            💡 <b>Learning Recommendation:</b> ${res.suggestedFix}
                        </div>
                    ` : ''}
                </div>
            `;
        }

        drawer.innerHTML = html;
    }
};
