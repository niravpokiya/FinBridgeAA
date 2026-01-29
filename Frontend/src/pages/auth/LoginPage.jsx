import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { ArrowRight, ArrowLeft } from 'lucide-react'
import { useAuthStore } from '../../store/authStore'
import { authAPI } from '../../lib/api'
import Button from '../../components/ui/Button'
import Input from '../../components/ui/Input'
import LoadingSpinner from '../../components/ui/LoadingSpinner'

export default function LoginPage() {
  const [step, setStep] = useState('phone') // phone | otp | success
  const [phoneNumber, setPhoneNumber] = useState('')
  const [otp, setOtp] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [otpSent, setOtpSent] = useState(false)
  
  const { login } = useAuthStore()
  const navigate = useNavigate()

  const handleRequestOTP = async (e) => {
    e.preventDefault()
    if (!phoneNumber || phoneNumber.length !== 10) {
      setError('Please enter a valid 10-digit phone number')
      return
    }

    setLoading(true)
    setError('')

    try {
      await authAPI.requestOTP(phoneNumber)
      setOtpSent(true)
      setStep('otp')
    } catch (err) {
      console.error('OTP request error:', err)
      setError('Failed to send OTP. Please try again.')
    } finally {
      setLoading(false)
    }
  }

  const handleVerifyOTP = async (e) => {
    e.preventDefault()
    if (!otp || otp.length !== 6) {
      setError('Please enter a valid 6-digit OTP')
      return
    }

    setLoading(true)
    setError('')

    try {
      const response = await authAPI.verifyOTP(phoneNumber, otp)
      const { access_token } = response.data
      
      // Create user object (you might want to fetch user details from API)
      const user = {
        id: 'temp-user-id',
        name: 'User',
        email: `${phoneNumber}@finbridge.com`,
        phone: phoneNumber
      }

      await login(access_token, user)
      setStep('success')
      
      // Redirect to dashboard after a short delay
      setTimeout(() => {
        navigate('/dashboard')
      }, 1500)
    } catch (err) {
      console.error('OTP verification error:', err)
      setError('Invalid OTP. Please try again.')
    } finally {
      setLoading(false)
    }
  }

  const handleBackToPhone = () => {
    setStep('phone')
    setOtp('')
    setError('')
  }

  const handleResendOTP = async () => {
    setLoading(true)
    setError('')

    try {
      await authAPI.requestOTP(phoneNumber)
      setOtpSent(true)
      // You might want to show a success message
    } catch (err) {
      console.error('Resend OTP error:', err)
      setError('Failed to resend OTP. Please try again.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-primary-600 to-purple-700 flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        {/* Logo */}
        <div className="text-center mb-8">
          <div className="w-16 h-16 bg-white/20 backdrop-blur-lg rounded-full flex items-center justify-center mx-auto mb-4">
            <svg className="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
            </svg>
          </div>
          <h1 className="text-3xl font-bold text-white mb-2">Welcome to FinBridge</h1>
          <p className="text-white/80">
            {step === 'phone' && 'Enter your phone number to continue' }
            {step === 'otp' && `Verify your number ${phoneNumber}` }
            {step === 'success' && 'Login successful!' }
          </p>
        </div>

        {/* Login Card */}
        <div className="bg-white/10 backdrop-blur-lg border border-white/20 rounded-2xl p-8">
          {step === 'phone' && (
            <form onSubmit={handleRequestOTP} className="space-y-6">
              <Input
                label="Phone Number"
                type="tel"
                placeholder="Enter 10-digit phone number"
                value={phoneNumber}
                onChange={(e) => setPhoneNumber(e.target.value.replace(/\D/g, ''))}
                maxLength={10}
                className="bg-white/10 border-white/20 text-white placeholder-white/60"
                labelClassName="text-white"
              />

              {error && (
                <div className="bg-red-500/20 border border-red-500/50 rounded-lg p-3">
                  <p className="text-red-100 text-sm">{error}</p>
                </div>
              )}

              <Button
                type="submit"
                loading={loading}
                disabled={loading || phoneNumber.length !== 10}
                className="w-full bg-white text-primary-600 hover:bg-gray-100"
              >
                {loading ? 'Sending OTP...' : 'Send OTP'}
                <ArrowRight className="ml-2 h-4 w-4" />
              </Button>
            </form>
          )}

          {step === 'otp' && (
            <form onSubmit={handleVerifyOTP} className="space-y-6">
              <div className="text-center mb-4">
                <p className="text-white/80 text-sm mb-4">
                  We've sent a 6-digit OTP to {phoneNumber}
                </p>
                {otpSent && (
                  <p className="text-green-300 text-sm">
                    OTP sent successfully! Check your messages.
                  </p>
                )}
              </div>

              <Input
                label="Enter OTP"
                type="text"
                placeholder="Enter 6-digit OTP"
                value={otp}
                onChange={(e) => setOtp(e.target.value.replace(/\D/g, ''))}
                maxLength={6}
                className="bg-white/10 border-white/20 text-white placeholder-white/60 text-center text-lg tracking-widest"
                labelClassName="text-white"
              />

              {error && (
                <div className="bg-red-500/20 border border-red-500/50 rounded-lg p-3">
                  <p className="text-red-100 text-sm">{error}</p>
                </div>
              )}

              <div className="space-y-3">
                <Button
                  type="submit"
                  loading={loading}
                  disabled={loading || otp.length !== 6}
                  className="w-full bg-white text-primary-600 hover:bg-gray-100"
                >
                  {loading ? 'Verifying...' : 'Verify & Login'}
                  <ArrowRight className="ml-2 h-4 w-4" />
                </Button>

                <div className="flex items-center justify-between">
                  <button
                    type="button"
                    onClick={handleBackToPhone}
                    className="text-white/80 hover:text-white text-sm flex items-center"
                  >
                    <ArrowLeft className="mr-1 h-3 w-3" />
                    Back
                  </button>
                  
                  <button
                    type="button"
                    onClick={handleResendOTP}
                    disabled={loading}
                    className="text-white/80 hover:text-white text-sm"
                  >
                    Resend OTP
                  </button>
                </div>
              </div>
            </form>
          )}

          {step === 'success' && (
            <div className="text-center space-y-6">
              <div className="w-16 h-16 bg-green-500 rounded-full flex items-center justify-center mx-auto">
                <svg className="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                </svg>
              </div>
              <div>
                <h3 className="text-xl font-semibold text-white mb-2">Welcome back!</h3>
                <p className="text-white/80">Redirecting to your dashboard...</p>
              </div>
              <LoadingSpinner size="lg" />
            </div>
          )}
        </div>

        {/* Trust Badges */}
        <div className="mt-8 text-center text-white/60 text-sm">
          <div className="flex items-center justify-center gap-6 mb-2">
            <div className="flex items-center">
              <svg className="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
              </svg>
              <span>RBI Licensed</span>
            </div>
            <div className="flex items-center">
              <svg className="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
              </svg>
              <span>256-bit Encryption</span>
            </div>
          </div>
          <p>By continuing, you agree to our Terms & Privacy Policy</p>
        </div>

        {/* Register Link */}
        <div className="mt-6 text-center">
          <p className="text-white/80">
            New to FinBridge?{' '}
            <Link to="/register" className="text-white hover:underline font-medium">
              Create an account
            </Link>
          </p>
        </div>
      </div>
    </div>
  )
}
