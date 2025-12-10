class CampusMatrixAuth {
    static API_BASE_URL = 'http://localhost:8080/api';
    
    static async register(userData) {
        try {
            const response = await fetch(`${this.API_BASE_URL}/auth/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userData)
            });
            
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Registration failed');
            }
            
            const data = await response.json();
            
            // Store auth data
            localStorage.setItem('campusMatrixToken', data.token);
            localStorage.setItem('campusMatrixUser', JSON.stringify(data.user));
            
            return {
                success: true,
                user: data.user,
                token: data.token
            };
            
        } catch (error) {
            console.error('Registration error:', error);
            return {
                success: false,
                message: error.message
            };
        }
    }
    
    static async login(credentials) {
        try {
            const response = await fetch(`${this.API_BASE_URL}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(credentials)
            });
            
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Login failed');
            }
            
            const data = await response.json();
            
            // Store auth data
            localStorage.setItem('campusMatrixToken', data.token);
            localStorage.setItem('campusMatrixUser', JSON.stringify(data.user));
            
            return {
                success: true,
                user: data.user,
                token: data.token
            };
            
        } catch (error) {
            console.error('Login error:', error);
            return {
                success: false,
                message: error.message
            };
        }
    }
    
    static logout() {
        localStorage.removeItem('campusMatrixToken');
        localStorage.removeItem('campusMatrixUser');
        window.location.href = 'login.html';
    }
    
    static getToken() {
        return localStorage.getItem('campusMatrixToken');
    }
    
    static getCurrentUser() {
        const userStr = localStorage.getItem('campusMatrixUser');
        return userStr ? JSON.parse(userStr) : null;
    }
    
    static isAuthenticated() {
        return !!this.getToken();
    }
    
    static async fetchWithAuth(url, options = {}) {
        const token = this.getToken();
        
        const defaultOptions = {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
                ...options.headers
            }
        };
        
        const mergedOptions = { ...options, ...defaultOptions };
        
        try {
            const response = await fetch(`${this.API_BASE_URL}${url}`, mergedOptions);
            
            if (response.status === 401) {
                this.logout();
                throw new Error('Session expired. Please login again.');
            }
            
            return response;
            
        } catch (error) {
            console.error('API call failed:', error);
            throw error;
        }
    }
    
    static async getCurrentUserProfile() {
        try {
            const response = await this.fetchWithAuth('/users/me');
            
            if (!response.ok) {
                throw new Error('Failed to fetch user profile');
            }
            
            return await response.json();
            
        } catch (error) {
            console.error('Profile fetch error:', error);
            throw error;
        }
    }
}

// Update your existing login/register forms to use this class
document.addEventListener('DOMContentLoaded', function() {
    // Login form submission
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            
            const result = await CampusMatrixAuth.login({ email, password });
            
            if (result.success) {
                alert('Login successful!');
                window.location.href = 'dashboard.html';
            } else {
                alert(`Login failed: ${result.message}`);
            }
        });
    }
    
    // Register form submission
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            
            const userData = {
                email: document.getElementById('email').value,
                password: document.getElementById('password').value,
                fullName: document.getElementById('fullName').value,
                department: document.getElementById('department').value,
                yearOfStudy: document.getElementById('yearOfStudy').value,
                studentId: document.getElementById('studentId').value,
                phoneNumber: document.getElementById('phoneNumber').value
            };
            
            const result = await CampusMatrixAuth.register(userData);
            
            if (result.success) {
                alert('Registration successful! You are now logged in.');
                window.location.href = 'dashboard.html';
            } else {
                alert(`Registration failed: ${result.message}`);
            }
        });
    }
    
    // Check authentication on dashboard
    if (window.location.pathname.includes('dashboard.html')) {
        if (!CampusMatrixAuth.isAuthenticated()) {
            alert('Please login first!');
            window.location.href = 'login.html';
        } else {
            // Load user data
            CampusMatrixAuth.getCurrentUserProfile()
                .then(user => {
                    // Update UI with user data
                    document.getElementById('welcomeMessage').textContent = `Hello, ${user.fullName}`;
                    document.getElementById('sidebarUserName').textContent = user.fullName;
                    document.getElementById('sidebarUserRole').textContent = `${user.department} Student`;
                    
                    // Set avatar initials
                    const initials = user.fullName.split(' ').map(n => n[0]).join('').toUpperCase();
                    document.getElementById('sidebarAvatar').textContent = initials;
                    document.getElementById('profilePic').textContent = initials;
                })
                .catch(error => {
                    console.error('Failed to load user profile:', error);
                });
        }
    }
});