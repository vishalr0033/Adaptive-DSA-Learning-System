/**
 * Chart.js Integration Wrappers for DSA Learning System
 */

const DsaCharts = {
    radarChart: null,
    barChart: null,
    mistakeChart: null,
    improvementChart: null,

    renderTopicRadar(canvasId, topicData) {
        const ctx = document.getElementById(canvasId);
        if (!ctx || typeof Chart === 'undefined') return;

        if (this.radarChart) {
            this.radarChart.destroy();
        }

        const labels = topicData.map(t => t.topicName);
        const data = topicData.map(t => t.accuracyPercentage);

        this.radarChart = new Chart(ctx, {
            type: 'radar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Accuracy %',
                    data: data,
                    backgroundColor: 'rgba(59, 130, 246, 0.25)',
                    borderColor: '#3b82f6',
                    borderWidth: 2,
                    pointBackgroundColor: '#06b6d4',
                    pointBorderColor: '#fff',
                    pointHoverBackgroundColor: '#fff',
                    pointHoverBorderColor: '#06b6d4'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    r: {
                        angleLines: { color: 'rgba(255, 255, 255, 0.1)' },
                        grid: { color: 'rgba(255, 255, 255, 0.1)' },
                        pointLabels: {
                            color: '#94a3b8',
                            font: { size: 11, family: 'Inter' }
                        },
                        ticks: {
                            backdropColor: 'transparent',
                            color: '#64748b',
                            min: 0,
                            max: 100,
                            stepSize: 20
                        }
                    }
                },
                plugins: {
                    legend: { display: false }
                }
            }
        });
    },

    renderMistakeDistribution(canvasId, mistakeMap) {
        const ctx = document.getElementById(canvasId);
        if (!ctx || typeof Chart === 'undefined') return;

        if (this.mistakeChart) {
            this.mistakeChart.destroy();
        }

        const labels = Object.keys(mistakeMap || {});
        const data = Object.values(mistakeMap || {});

        if (labels.length === 0) {
            labels.push('No Mistakes Yet');
            data.push(1);
        }

        this.mistakeChart = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: labels,
                datasets: [{
                    data: data,
                    backgroundColor: [
                        '#f43f5e',
                        '#f59e0b',
                        '#8b5cf6',
                        '#3b82f6',
                        '#06b6d4',
                        '#10b981',
                        '#ec4899'
                    ],
                    borderWidth: 2,
                    borderColor: '#1e293b'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'right',
                        labels: {
                            color: '#cbd5e1',
                            font: { size: 12, family: 'Inter' },
                            padding: 12
                        }
                    }
                }
            }
        });
    },

    renderImprovementChart(canvasId, topicCards) {
        const ctx = document.getElementById(canvasId);
        if (!ctx || typeof Chart === 'undefined') return;

        if (this.improvementChart) {
            this.improvementChart.destroy();
        }

        const labels = topicCards.map(c => c.topicName);
        const preData = topicCards.map(c => c.preAccuracy);
        const postData = topicCards.map(c => c.postAccuracy);

        this.improvementChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [
                    {
                        label: 'Before Practice (%)',
                        data: preData,
                        backgroundColor: 'rgba(244, 63, 94, 0.7)',
                        borderRadius: 6
                    },
                    {
                        label: 'After Practice (%)',
                        data: postData,
                        backgroundColor: 'rgba(16, 185, 129, 0.85)',
                        borderRadius: 6
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    x: {
                        grid: { color: 'rgba(255, 255, 255, 0.05)' },
                        ticks: { color: '#94a3b8', font: { family: 'Inter' } }
                    },
                    y: {
                        min: 0,
                        max: 100,
                        grid: { color: 'rgba(255, 255, 255, 0.08)' },
                        ticks: {
                            color: '#94a3b8',
                            font: { family: 'Inter' },
                            callback: v => v + '%'
                        }
                    }
                },
                plugins: {
                    legend: {
                        labels: { color: '#cbd5e1', font: { family: 'Inter' } }
                    }
                }
            }
        });
    }
};
