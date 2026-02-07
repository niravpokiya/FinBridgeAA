import { Routes, Route, Navigate } from 'react-router-dom'
import { useEffect } from 'react'
import { useAuthStore } from './store/authStore'
import Layout from './components/layout/Layout'
import LandingPage from './pages/LandingPage'
import LoginPage from './pages/auth/LoginPage'
import RegisterPage from './pages/auth/RegisterPage'
import DashboardPage from './pages/dashboard/DashboardPage'
import LoansPage from './pages/loans/LoansPage'
import BudgetPage from './pages/budget/BudgetPage'
import ProfilePage from './pages/profile/ProfilePage'
import ConsentManagementPage from './pages/consent/ConsentManagementPage'
import LoadingSpinner from './components/ui/LoadingSpinner'

function App() {
  const { isAuthenticated, isLoading, checkAuth } = useAuthStore()

  // Initialize auth state on app load
  useEffect(() => {
    checkAuth()
  }, [checkAuth])

  // Temporarily bypass authentication for testing
  const isAuthTemp = true // isAuthenticated

  if (isLoading) {
    return <LoadingSpinner />
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <Routes>
        {/* Public Routes */}
        <Route 
          path="/" 
          element={isAuthTemp ? <Navigate to="/dashboard" /> : <LandingPage />} 
        />
        <Route 
          path="/login" 
          element={isAuthTemp ? <Navigate to="/dashboard" /> : <LoginPage />} 
        />
        <Route 
          path="/register" 
          element={isAuthTemp ? <Navigate to="/dashboard" /> : <RegisterPage />} 
        />

        {/* Protected Routes with Layout */}
        <Route 
          path="/" 
          element={isAuthTemp ? <Layout /> : <Navigate to="/login" />}
        >
          <Route path="dashboard" element={<DashboardPage />} />
          <Route path="loans" element={<LoansPage />} />
          <Route path="budget" element={<BudgetPage />} />
          <Route path="profile" element={<ProfilePage />} />
          <Route path="consents" element={<ConsentManagementPage />} />
        </Route>

        {/* Fallback */}
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </div>
  )
}

export default App
