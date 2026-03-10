function switchTab(tab) {
    document.querySelectorAll('.tab-panel').forEach(p => p.classList.remove('active'));
    document.querySelectorAll('.sidebar-menu a').forEach(a => a.classList.remove('active'));
    document.getElementById('panel-' + tab).classList.add('active');
    document.getElementById('tab-' + tab).classList.add('active');
}
const hash = window.location.hash.replace('#', '');
if (['experience', 'education', 'certifications', 'skills', 'projects'].includes(hash)) switchTab(hash);

// Edit Experience Toggle
function toggleEditExperience(id) {
    const formContainer = document.getElementById('edit-experience-' + id);
    if (formContainer) {
        if (formContainer.style.display === 'none') {
            formContainer.style.display = 'block';
        } else {
            formContainer.style.display = 'none';
        }
    }
}

// Edit Education Toggle
function toggleEditEducation(id) {
    const formContainer = document.getElementById('edit-education-' + id);
    if (formContainer) {
        if (formContainer.style.display === 'none') {
            formContainer.style.display = 'block';
        } else {
            formContainer.style.display = 'none';
        }
    }
}

// Edit Project Toggle
function toggleEditProject(id) {
    const formContainer = document.getElementById('edit-project-' + id);
    if (formContainer) {
        if (formContainer.style.display === 'none') {
            formContainer.style.display = 'block';
        } else {
            formContainer.style.display = 'none';
        }
    }
}

// Bullet point auto-formatting for textareas
document.addEventListener('DOMContentLoaded', function () {
    const textareas = document.querySelectorAll('textarea[name="description"]');
    textareas.forEach(textarea => {
        textarea.addEventListener('keydown', function (e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                const start = this.selectionStart;
                const end = this.selectionEnd;
                const value = this.value;

                // If it's the very first line and empty, add bullet
                if (value.trim() === '') {
                    this.value = '• ';
                    this.selectionStart = this.selectionEnd = 2;
                } else {
                    // Insert newline and bullet
                    this.value = value.substring(0, start) + '\n• ' + value.substring(end);
                    this.selectionStart = this.selectionEnd = start + 3;
                }
            }
        });

        // Also add bullet when first focusing if empty
        textarea.addEventListener('focus', function (e) {
            if (this.value.trim() === '') {
                this.value = '• ';
            }
        });
    });

    // End Date required toggle logic for experiences
    function setupEndDateToggle(checkboxElement, endDateInput, reqSpan) {
        function toggleRequired() {
            if (checkboxElement.checked) {
                endDateInput.removeAttribute('required');
                if (reqSpan) reqSpan.style.display = 'none';
            } else {
                endDateInput.setAttribute('required', 'required');
                if (reqSpan) reqSpan.style.display = 'inline';
            }
        }
        checkboxElement.addEventListener('change', toggleRequired);
        toggleRequired(); // Run on init
    }

    const currentExpCheckboxes = document.querySelectorAll('input[type="checkbox"][name="current"]');
    currentExpCheckboxes.forEach(cb => {
        const form = cb.closest('form');
        if (form) {
            const endInput = form.querySelector('input[name="endDate"]');
            const label = endInput ? endInput.closest('.form-group').querySelector('.form-label') : null;
            if (endInput && label) {
                const reqSpan = label.querySelector('.end-date-req');
                setupEndDateToggle(cb, endInput, reqSpan);
            }
        }

        // Enforce mutual exclusivity
        cb.addEventListener('change', function () {
            if (this.checked) {
                currentExpCheckboxes.forEach(otherCb => {
                    if (otherCb !== this && otherCb.checked) {
                        otherCb.checked = false;
                        // trigger change event to re-evaluate required status
                        otherCb.dispatchEvent(new Event('change'));
                    }
                });
            }
        });
    });
});

// Custom skill level dropdown
function toggleSkillDropdown() {
    document.getElementById('skillLevelDropdown').classList.toggle('open');
}
function selectSkillLevel(value, label) {
    document.getElementById('skillLevelInput').value = value;
    document.getElementById('skillLevelLabel').textContent = label;
    const dropdown = document.getElementById('skillLevelDropdown');
    dropdown.classList.remove('open');
    // Mark selected option
    dropdown.querySelectorAll('.custom-select-option').forEach(opt => {
        opt.classList.toggle('selected', opt.textContent === label);
    });
}

// Custom skill type combobox
function openSkillTypeDropdown() {
    document.getElementById('skillTypeDropdown').classList.add('open');
    filterSkillTypes();
}
function selectSkillType(val) {
    const input = document.getElementById('skillTypeInput');
    input.value = val;
    document.getElementById('skillTypeDropdown').classList.remove('open');
    input.dispatchEvent(new Event('input', { bubbles: true }));
    populateSkillNames(val);
}
function filterSkillTypes() {
    const inputVal = document.getElementById('skillTypeInput').value.toLowerCase();
    const options = document.querySelectorAll('#skillTypeOptions .custom-select-option');
    options.forEach(opt => {
        if (opt.textContent.toLowerCase().includes(inputVal)) {
            opt.style.display = 'block';
        } else {
            opt.style.display = 'none';
        }
    });
}

// Custom skill name combobox dictionary mapping
const skillMappings = {
    "Programming Languages": ["C", "C++", "C#", "Java", "Python", "JavaScript", "TypeScript", "Go (Golang)", "Rust", "Kotlin", "Swift", "PHP", "Ruby", "Dart", "R", "MATLAB", "Scala"],
    "Frameworks & Libraries": ["React", "Vue.js", "Angular", "Next.js / Nuxt.js", "Svelte / SvelteKit", "Express.js", "NestJS", "Spring Boot (Java)", "Django (Python)", "Flask (Python)", "Ruby on Rails", "Laravel (PHP)", ".NET / ASP.NET Core", "TensorFlow", "PyTorch", "Scikit-learn", "jQuery (legacy but common)"],
    "Web Development (front + back)": ["HTML5", "CSS3 / Flexbox / Grid", "Sass / Less", "Responsive design / Media queries", "Accessibility (WCAG)", "Progressive Web Apps (PWA)", "REST APIs", "GraphQL", "WebSockets", "Server-Side Rendering (SSR) / Static Site Generation (SSG)", "Web performance optimization", "Authentication (OAuth, JWT)"],
    "Databases & Data Storage": ["MySQL / MariaDB", "PostgreSQL", "SQLite", "MongoDB", "Redis (in-memory)", "Cassandra", "DynamoDB (AWS)", "ElasticSearch / OpenSearch", "Firebase Realtime / Firestore", "Oracle DB", "Neo4j (graph DB)"],
    "Cloud & DevOps / Infrastructure": ["Amazon Web Services (AWS)", "Microsoft Azure", "Google Cloud Platform (GCP)", "Docker (containers)", "Kubernetes (k8s)", "Terraform (IaC)", "Ansible / Chef / Puppet", "CI/CD: Jenkins, GitHub Actions, GitLab CI", "Nginx / HAProxy", "Prometheus + Grafana (monitoring)", "Cloud security / IAM"],
    "Data Science, ML & Analytics": ["NumPy", "Pandas", "Matplotlib / Seaborn (visualization)", "Scikit-learn", "TensorFlow / Keras", "PyTorch", "OpenCV (computer vision)", "NLP libraries: spaCy, Hugging Face Transformers", "SQL for analytics", "Big Data: Spark, Hadoop", "ML model deployment (MLflow, TorchServe)"],
    "Tools, IDEs & Productivity Software": ["Git (version control)", "GitHub / GitLab / Bitbucket", "Visual Studio Code", "IntelliJ IDEA / PyCharm", "Postman / Insomnia", "Docker Desktop", "VS (Visual Studio)", "Figma", "Notion", "JIRA / Trello", "Slack / Microsoft Teams", "OBS (streaming / demos)"],
    "UI / UX & Design": ["Figma (design & prototyping)", "Adobe XD / Sketch / Photoshop / Illustrator", "Wireframing", "Prototyping & user flows", "Usability testing / UX research", "Design systems / component libraries", "Interaction design / micro-interactions", "Information architecture"],
    "Mobile & Cross-Platform": ["Android (Java / Kotlin)", "iOS (Swift / SwiftUI)", "Flutter (Dart)", "React Native", "Xamarin / .NET MAUI", "Mobile app distribution", "Mobile testing & debugging tools"],
    "Operating Systems & System Software": ["Linux (Ubuntu, Arch, CentOS)", "Windows (desktop/server)", "macOS", "Shell scripting: Bash, Zsh", "System administration", "Virtualization: VirtualBox, VMware", "Containers & orchestration"],
    "Cybersecurity & Networking": ["Network fundamentals (TCP/IP, DNS, HTTP/S)", "Firewalls & routing basics", "Penetration testing / ethical hacking", "OWASP top 10 / web app security", "Cryptography basics (TLS, hashing)", "SAST / DAST tools", "SIEM / log analysis"],
    "Testing & Quality Assurance": ["Unit testing (JUnit, pytest, Jest)", "Integration testing", "End-to-end (E2E) testing: Cypress, Selenium, Playwright", "Test automation frameworks", "Mocking & stubbing", "Test-driven development (TDD)", "Performance testing (JMeter, k6)"],
    "Project Management & Collaboration": ["Agile methodologies (Scrum, Kanban)", "Sprint planning / backlog grooming", "Jira / Trello / Asana", "Roadmapping / release planning", "Requirement gathering / stakeholder communication", "CI/CD pipeline management", "Documentation: Markdown, Confluence"],
    "Embedded Systems, IoT & Hardware": ["Microcontrollers: Arduino, ESP32, STM32", "Embedded C / C++", "RTOS basics (FreeRTOS)", "Wiring / PCB basics", "Sensors & actuators integration", "MQTT / CoAP (IoT protocols)", "Edge computing fundamentals"],
    "Soft / Professional Skills": ["Communication (verbal & written)", "Teamwork / collaboration", "Leadership / mentoring", "Problem solving & debugging", "Time management / prioritization", "Technical writing & documentation", "Presentation / public speaking"]
};

function populateSkillNames(typeVal) {
    const optionsContainer = document.getElementById('skillNameOptions');
    const skills = skillMappings[typeVal] || [];
    if (skills.length === 0) {
        optionsContainer.innerHTML = '<div class="custom-select-option" style="color: var(--text-muted); cursor: default; user-select: none;">Type to add custom skill</div>';
        return;
    }
    optionsContainer.innerHTML = skills.map(skill =>
        `<div class="custom-select-option" onclick="selectSkillName('${skill.replace(/'/g, "\\\\'")}')">${skill}</div>`
    ).join('');
}

function openSkillNameDropdown() {
    document.getElementById('skillNameDropdown').classList.add('open');
    filterSkillNames();
}
function selectSkillName(val) {
    const input = document.getElementById('skillNameInput');
    input.value = val;
    document.getElementById('skillNameDropdown').classList.remove('open');
    input.dispatchEvent(new Event('input', { bubbles: true }));
}
function filterSkillNames() {
    const inputVal = document.getElementById('skillNameInput').value.toLowerCase();
    const options = document.querySelectorAll('#skillNameOptions .custom-select-option');
    options.forEach(opt => {
        if (opt.hasAttribute('onclick') && opt.textContent.toLowerCase().includes(inputVal)) {
            opt.style.display = 'block';
        } else if (!opt.hasAttribute('onclick')) {
            opt.style.display = 'block'; // Helper messages stay visible
        } else {
            opt.style.display = 'none';
        }
    });
}

// Link type manual input to re-populate skill names
document.getElementById('skillTypeInput').addEventListener('input', function (e) {
    let matchedType = null;
    document.querySelectorAll('#skillTypeOptions .custom-select-option').forEach(opt => {
        if (opt.textContent === this.value) {
            matchedType = opt.textContent;
        }
    });
    populateSkillNames(matchedType || "Unknown");
});

// Auto-detect skill type from skill name
document.getElementById('skillNameInput').addEventListener('input', function (e) {
    const skillName = this.value.trim().toLowerCase();
    if (!skillName) return;

    // Check if skill exists in any type
    let foundType = null;
    for (const [type, skills] of Object.entries(skillMappings)) {
        if (skills.some(s => s.toLowerCase() === skillName)) {
            foundType = type;
            break;
        }
    }

    if (foundType) {
        const typeInput = document.getElementById('skillTypeInput');
        if (typeInput.value !== foundType) {
            typeInput.value = foundType;
            populateSkillNames(foundType); // Ensure choices update quietly
        }
    }
});

// Close dropdowns when clicking outside
document.addEventListener('click', function (e) {
    const levelDd = document.getElementById('skillLevelDropdown');
    if (levelDd && !levelDd.contains(e.target)) levelDd.classList.remove('open');

    const typeDd = document.getElementById('skillTypeDropdown');
    if (typeDd && !typeDd.contains(e.target)) typeDd.classList.remove('open');

    const nameDd = document.getElementById('skillNameDropdown');
    if (nameDd && !nameDd.contains(e.target)) nameDd.classList.remove('open');
});

// Save Profile — AJAX, no page reload, smart disable until next change
(function () {
    const form = document.getElementById('profileForm');
    const btn = document.getElementById('saveProfileBtn');
    if (!form || !btn) return;

    // Snapshot of values that are in DB (on page load = what's currently saved)
    let savedValues = {};
    function snapshot() {
        form.querySelectorAll('input, textarea').forEach(el => {
            savedValues[el.name] = el.value;
        });
    }
    snapshot();

    // Disable button — nothing to save yet
    function setClean() {
        btn.disabled = true;
        btn.textContent = '✓ Saved';
        btn.style.opacity = '0.5';
        btn.style.cursor = 'not-allowed';
    }

    // Enable button — unsaved changes exist
    function setDirty() {
        btn.disabled = false;
        btn.textContent = 'Save Profile';
        btn.style.opacity = '1';
        btn.style.cursor = 'pointer';
    }

    function checkDirty() {
        let dirty = false;
        form.querySelectorAll('input, textarea').forEach(el => {
            if (el.value !== (savedValues[el.name] ?? '')) dirty = true;
        });
        dirty ? setDirty() : setClean();
    }

    // Start clean if profile already has data, otherwise start dirty
    const hasExistingData = Object.values(savedValues).some(v => v && v.trim() !== '');
    if (hasExistingData) {
        setClean();
    } else {
        setDirty();
    }

    form.querySelectorAll('input, textarea').forEach(el => {
        el.addEventListener('input', checkDirty);
    });

    form.addEventListener('submit', async function (e) {
        e.preventDefault();
        btn.disabled = true;
        btn.textContent = '⏳ Saving...';
        btn.style.opacity = '0.6';

        try {
            const data = new FormData(form);
            const res = await fetch('/dashboard/profile/save', {
                method: 'POST',
                body: data
            });
            const json = await res.json();
            if (json.success) {
                snapshot(); // update saved baseline
                setClean();
            } else {
                btn.disabled = false;
                btn.textContent = '⚠ Error — Retry';
                btn.style.opacity = '1';
                btn.style.cursor = 'pointer';
            }
        } catch (err) {
            btn.disabled = false;
            btn.textContent = '⚠ Error — Retry';
            btn.style.opacity = '1';
            btn.style.cursor = 'pointer';
        }
    });
})();

// Profile picture upload — instant preview + AJAX save
(function () {
    const fileInput = document.getElementById('picFileInput');
    if (!fileInput) return;

    fileInput.addEventListener('change', async function () {
        const file = fileInput.files[0];
        if (!file) return;

        const status = document.getElementById('picUploadStatus');

        // Client-side size guard (5 MB)
        if (file.size > 5 * 1024 * 1024) {
            status.textContent = '⚠ File too large — max 5 MB';
            status.style.color = '#ef4444';
            fileInput.value = '';
            return;
        }

        const localUrl = URL.createObjectURL(file);
        updateAvatarPreview(localUrl);
        status.textContent = '⏳ Uploading…';
        status.style.color = 'var(--text-muted)';

        // POST to server
        const fd = new FormData();
        fd.append('file', file);
        try {
            const res = await fetch('/dashboard/profile/upload-pic', {
                method: 'POST',
                body: fd
            });
            const json = await res.json();
            if (json.success) {
                updateAvatarPreview(json.picUrl);
                status.textContent = '✓ Photo saved';
                status.style.color = '#22c55e';
                setTimeout(() => {
                    status.textContent = 'Click photo to change';
                    status.style.color = 'var(--text-muted)';
                }, 3000);
            } else {
                status.textContent = '⚠ Upload failed — try again';
                status.style.color = '#ef4444';
            }
        } catch (err) {
            status.textContent = '⚠ Upload failed — try again';
            status.style.color = '#ef4444';
        }
        // Reset so the same file can be re-selected if needed
        fileInput.value = '';
    });

    function updateAvatarPreview(url) {
        // Update the main profile-tab preview
        let preview = document.getElementById('picPreview');
        const placeholder = document.getElementById('picPlaceholder');
        const wrapper = document.querySelector('.pic-upload-wrapper');

        if (!preview) {
            // First upload — create the img element
            preview = document.createElement('img');
            preview.id = 'picPreview';
            if (placeholder) {
                wrapper.insertBefore(preview, placeholder);
                placeholder.style.display = 'none';
            } else {
                wrapper.insertBefore(preview, wrapper.firstChild);
            }
        }
        preview.src = url;
        preview.style.display = 'block';
        if (placeholder) placeholder.style.display = 'none';

        // Update sidebar avatar
        const sidebarAvatar = document.getElementById('sidebarAvatar');
        if (sidebarAvatar) {
            let sidebarImg = document.getElementById('sidebarAvatarImg');
            const sidebarInitial = document.getElementById('sidebarAvatarInitial');
            if (!sidebarImg) {
                sidebarImg = document.createElement('img');
                sidebarImg.id = 'sidebarAvatarImg';
                sidebarAvatar.appendChild(sidebarImg);
            }
            sidebarImg.src = url;
            sidebarImg.style.display = 'block';
            if (sidebarInitial) sidebarInitial.style.display = 'none';
        }
    }
})();

// ===== Certificate File Handling =====

// Show chosen filename in the Add Certification form
function previewNewCertFile(input) {
    const label = document.getElementById('newCertFileLabel');
    label.textContent = input.files[0] ? input.files[0].name : 'No file chosen';
}

// Add Certification form — normal submit when no file; AJAX create+upload when file chosen
(function () {
    const certForm = document.querySelector('form[action="/dashboard/certification/add"]');
    if (!certForm) return;
    certForm.addEventListener('submit', async function (e) {
        const fileInput = document.getElementById('newCertFile');
        const hasFile = fileInput && fileInput.files[0];

        // No file chosen — let the form POST normally, no AJAX needed
        if (!hasFile) return;

        // File chosen — intercept: AJAX create cert, upload file, then redirect
        e.preventDefault();
        const btn = document.getElementById('addCertBtn');
        const statusEl = document.getElementById('addCertStatus');
        btn.disabled = true;
        btn.textContent = '⏳ Saving...';
        if (statusEl) statusEl.textContent = '';

        // Step 1 — create the certification via AJAX to get the new certId
        const certData = new FormData(certForm);
        let certId = null;
        try {
            const res = await fetch('/dashboard/certification/add-json', {
                method: 'POST', body: certData
            });
            const json = await res.json();
            if (!json.success) throw new Error(json.error || 'Save failed');
            certId = json.certId;
        } catch (err) {
            btn.disabled = false;
            btn.textContent = 'Add Certification';
            if (statusEl) { statusEl.textContent = '⚠ ' + err.message; statusEl.style.color = '#ef4444'; }
            return;
        }

        // Step 2 — upload the file to the newly created cert
        if (statusEl) { statusEl.textContent = '⏳ Uploading file…'; statusEl.style.color = 'var(--text-muted)'; }
        const fd = new FormData();
        fd.append('certId', certId);
        fd.append('file', fileInput.files[0]);
        try {
            const res = await fetch('/dashboard/certification/upload-file', {
                method: 'POST', body: fd
            });
            const json = await res.json();
            if (!json.success) throw new Error(json.error || 'Upload failed');
        } catch (err) {
            // cert saved, file failed — redirect anyway, user can retry via Attach on card
            if (statusEl) { statusEl.textContent = '⚠ File upload failed — cert saved'; statusEl.style.color = '#ffc146'; }
        }

        // Step 3 — redirect
        window.location.href = '/dashboard#certifications';
        window.location.reload();
    });
})();

// AJAX upload for an existing cert card's "Attach / Replace" file input
async function uploadCertFile(input, certId) {
    const file = input.files[0];
    if (!file) return;
    const statusEl = document.getElementById('certFileStatus_' + certId);
    if (statusEl) { statusEl.textContent = '⏳ Uploading…'; statusEl.style.color = 'var(--text-muted)'; }

    const fd = new FormData();
    fd.append('certId', certId);
    fd.append('file', file);

    try {
        const res = await fetch('/dashboard/certification/upload-file', { method: 'POST', body: fd });
        const json = await res.json();
        if (json.success) {
            if (statusEl) { statusEl.textContent = '✓ Saved'; statusEl.style.color = '#22c55e'; }
            // Update / insert the View Certificate link without reloading
            const card = input.closest('.entry-card');
            if (card) {
                let viewLink = card.querySelector('a[data-cert-file]');
                if (!viewLink) {
                    viewLink = document.createElement('a');
                    viewLink.setAttribute('data-cert-file', 'true');
                    viewLink.target = '_blank';
                    viewLink.style.cssText = 'font-size:0.78rem;color:#43e6fc;display:inline-flex;align-items:center;gap:0.25rem;';
                    const linksDiv = input.closest('.entry-info').querySelector('div[style*="flex"]');
                    if (linksDiv) linksDiv.insertBefore(viewLink, linksDiv.querySelector('label'));
                }
                viewLink.href = json.fileUrl;
                viewLink.textContent = '📄 View Certificate';
                // Switch label to "Replace"
                const lbl = card.querySelector('label[for="certFile_' + certId + '"] span');
                if (lbl) lbl.textContent = 'Replace';
            }
            setTimeout(() => { if (statusEl) statusEl.textContent = ''; }, 3000);
        } else {
            if (statusEl) { statusEl.textContent = '⚠ Upload failed'; statusEl.style.color = '#ef4444'; }
        }
    } catch (err) {
        if (statusEl) { statusEl.textContent = '⚠ Upload failed'; statusEl.style.color = '#ef4444'; }
    }
    input.value = '';
}
