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
  getConsentRequests: () => {
    console.log('Returning dummy consent requests')
    return Promise.resolve({
      data: [
        {
          id: 'REQ-001',
          fiName: 'HDFC Bank',
          purpose: 'Account aggregation for loan eligibility',
          createdDate: '2024-01-20',
          expiryDate: '2024-04-20',
          status: 'PENDING',
          dataTypes: ['Account Balance', 'Transaction History']
        }
      ]
    })
  },
  
  // Get user's active consents
  getActiveConsent: (userId) => {
    console.log(`Returning dummy active consents for user: ${userId}`)
    return Promise.resolve({
      data: [
        {
          id: 'CONSENT-001',
          fiName: 'HDFC Bank',
          status: 'ACTIVE',
          createdDate: '2024-01-15',
          expiryDate: '2024-04-15',
          dataTypes: ['Account Balance', 'Transaction History']
        },
        {
          id: 'CONSENT-002', 
          fiName: 'ICICI Bank',
          status: 'EXPIRED',
          createdDate: '2023-12-01',
          expiryDate: '2024-01-01',
          dataTypes: ['Account Balance']
        },
        {
          id: 'CONSENT-003',
          fiName: 'SBI Bank',
          status: 'REVOKED',
          createdDate: '2023-11-15',
          expiryDate: '2024-02-15',
          dataTypes: ['Account Balance', 'Transaction History', 'Loan Details']
        }
      ]
    })
  },
  
  // Approve a consent request
  approveConsent: (consentId) => {
    console.log(`Dummy approve consent: ${consentId}`)
    return Promise.resolve({ success: true })
  },
  
  // Reject a consent request
  rejectConsent: (consentId) => {
    console.log(`Dummy reject consent: ${consentId}`)
    return Promise.resolve({ success: true })
  },
  
  // Revoke an active consent
  revokeConsent: (consentId) => {
    console.log(`Dummy revoke consent: ${consentId}`)
    return Promise.resolve({ success: true })
  },
  
  // Get consent history
  getConsentHistory: (userId) => {
    console.log(`Returning dummy consent history for user: ${userId}`)
    return Promise.resolve({
      data: [
        {
          id: 'HIST-001',
          action: 'CREATED',
          consentId: 'CONSENT-001',
          timestamp: '2024-01-15T10:30:00Z',
          details: 'Consent created with HDFC Bank'
        }
      ]
    })
  },
  
  // Legacy endpoints (for backward compatibility)
  createConsent: (data) => {
    console.log('Dummy create consent:', data)
    return Promise.resolve({ 
      success: true, 
      id: `CONSENT-${Date.now()}`,
      ...data 
    })
  },
  declineConsent: (consentId) => {
    console.log(`Dummy decline consent: ${consentId}`)
    return Promise.resolve({ success: true })
  },
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
