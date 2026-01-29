import axios from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

// Create axios instance
const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Response interceptor to handle errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token expired or invalid
      localStorage.removeItem('accessToken')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// Auth API
export const authAPI = {
  requestOTP: (phoneNumber) => api.post('/auth/request-otp', { phoneNumber }),
  verifyOTP: (phoneNumber, otp) => api.post('/auth/verify-otp', { phoneNumber, otp }),
  logout: () => api.post('/auth/logout'),
}

// User API
export const userAPI = {
  getProfile: () => api.get('/users/user/profile'),
  updateProfile: (data) => api.put('/users/user/profile', data),
  getKycStatus: () => api.get('/users/user/kyc-status'),
}

// Loan API
export const loanAPI = {
  checkEligibility: (data) => api.post('/fiu/loan/check-eligibility', data),
  getHistory: () => api.get('/fiu/loan/history'),
}

// Budget API
export const budgetAPI = {
  analyze: (data) => api.post('/fiu/budget/analyze', data),
}

// Consent API
export const consentAPI = {
  // Get pending consent requests from FIPs
  getConsentRequests: () => api.get('/consent/requests'),
  
  // Get user's active consents
  getActiveConsent: (userId) => api.get(`/consent/consents/${userId}`),
  
  // Approve a consent request
  approveConsent: (consentId) => api.post(`/consent/consents/${consentId}/approve`),
  
  // Reject a consent request
  rejectConsent: (consentId) => api.post(`/consent/consents/${consentId}/reject`),
  
  // Revoke an active consent
  revokeConsent: (consentId) => api.post(`/consent/consents/${consentId}/revoke`),
  
  // Get consent history
  getConsentHistory: (userId) => api.get(`/consent/consents/${userId}/history`),
  
  // Legacy endpoints (for backward compatibility)
  createConsent: (data) => api.post('/consent/consents', data),
  declineConsent: (consentId) => api.post(`/consent/consents/${consentId}/decline`),
}

// Account Aggregator API
export const aaAPI = {
  fetchData: (data) => api.post('/aa/fetch-data', data),
}

// FIP (Financial Information Provider) API
export const fipAPI = {
  getFinancialData: (userId) => api.get(`/fip/fip/financial-data/${userId}`),
}

export default api
