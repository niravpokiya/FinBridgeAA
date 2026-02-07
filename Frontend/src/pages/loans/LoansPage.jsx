import React, { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { CreditCard, Plus, CheckCircle, Clock, XCircle, TrendingUp } from 'lucide-react'
import { loanAPI } from '../../lib/api'
import { formatCurrency, formatDate } from '../../lib/utils'
import Button from '../../components/ui/Button'
import Card from '../../components/ui/Card'
import CardHeader from '../../components/ui/Card'
import CardTitle from '../../components/ui/Card'
import LoadingSpinner from '../../components/ui/LoadingSpinner'

export default function LoansPage() {
  const [showApplicationForm, setShowApplicationForm] = useState(false)
  const [loanRequest, setLoanRequest] = useState({
    requestedAmount: 50000,
    tenureMonths: 12,
    purpose: 'personal'
  })

  const queryClient = useQueryClient()

  const { data: loanHistory, isLoading } = useQuery({
    queryKey: ['loanHistory'],
    queryFn: loanAPI.getHistory,
  })

  const checkEligibilityMutation = useMutation({
    mutationFn: loanAPI.checkEligibility,
    onSuccess: (data) => {
      console.log('Loan eligibility result:', data)
      // Handle successful eligibility check
    },
    onError: (error) => {
      console.error('Loan eligibility error:', error)
    },
  })

  const handleCheckEligibility = () => {
    checkEligibilityMutation.mutate(loanRequest)
  }

  const getStatusIcon = (status) => {
    switch (status) {
      case 'APPROVED':
        return <CheckCircle className="h-5 w-5 text-success-600" />
      case 'PENDING':
        return <Clock className="h-5 w-5 text-warning-600" />
      case 'REJECTED':
        return <XCircle className="h-5 w-5 text-error-600" />
      default:
        return <Clock className="h-5 w-5 text-gray-600" />
    }
  }

  const getStatusColor = (status) => {
    switch (status) {
      case 'APPROVED':
        return 'bg-success-100 text-success-800'
      case 'PENDING':
        return 'bg-warning-100 text-warning-800'
      case 'REJECTED':
        return 'bg-error-100 text-error-800'
      default:
        return 'bg-gray-100 text-gray-800'
    }
  }

  if (isLoading) {
    return (
      <div className="flex items-center justify-center p-6">
        <LoadingSpinner />
      </div>
    )
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Loans</h1>
          <p className="text-gray-600 mt-1">Manage your loan applications and eligibility</p>
        </div>
        <Button onClick={() => setShowApplicationForm(!showApplicationForm)}>
          <Plus className="h-4 w-4 mr-2" />
          New Application
        </Button>
      </div>

      {/* Loan Application Form */}
      {showApplicationForm && (
        <Card>
          <CardHeader>
            <CardTitle>Check Loan Eligibility</CardTitle>
          </CardHeader>
          <div className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Loan Amount
                </label>
                <div className="relative">
                  <span className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500">
                    ₹
                  </span>
                  <input
                    type="number"
                    value={loanRequest.requestedAmount}
                    onChange={(e) => setLoanRequest(prev => ({
                      ...prev,
                      requestedAmount: parseInt(e.target.value) || 0
                    }))}
                    className="pl-8 w-full input"
                    min={10000}
                    max={1000000}
                    step={10000}
                  />
                </div>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Tenure (Months)
                </label>
                <select
                  value={loanRequest.tenureMonths}
                  onChange={(e) => setLoanRequest(prev => ({
                    ...prev,
                    tenureMonths: parseInt(e.target.value)
                  }))}
                  className="w-full input"
                >
                  <option value={6}>6 Months</option>
                  <option value={12}>12 Months</option>
                  <option value={24}>24 Months</option>
                  <option value={36}>36 Months</option>
                  <option value={48}>48 Months</option>
                  <option value={60}>60 Months</option>
                </select>
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Loan Purpose
              </label>
              <select
                value={loanRequest.purpose}
                onChange={(e) => setLoanRequest(prev => ({
                  ...prev,
                  purpose: e.target.value
                }))}
                className="w-full input"
              >
                <option value="personal">Personal Loan</option>
                <option value="home">Home Loan</option>
                <option value="education">Education Loan</option>
                <option value="business">Business Loan</option>
                <option value="vehicle">Vehicle Loan</option>
              </select>
            </div>

            {/* Eligibility Result */}
            {checkEligibilityMutation.data && (
              <div className="p-4 bg-blue-50 border border-blue-200 rounded-lg">
                <div className="flex items-center mb-2">
                  <TrendingUp className="h-5 w-5 text-blue-600 mr-2" />
                  <h4 className="font-medium text-blue-900">Eligibility Result</h4>
                </div>
                <div className="space-y-2 text-sm text-blue-800">
                  <p><strong>Status:</strong> {checkEligibilityMutation.data.eligible ? 'Eligible' : 'Not Eligible'}</p>
                  <p><strong>Max Amount:</strong> {formatCurrency(checkEligibilityMutation.data.maxAmount || 0)}</p>
                  <p><strong>Interest Rate:</strong> {checkEligibilityMutation.data.interestRate || 'N/A'}%</p>
                  <p><strong>EMI:</strong> {formatCurrency(checkEligibilityMutation.data.emi || 0)}</p>
                </div>
              </div>
            )}

            <div className="flex gap-3">
              <Button
                onClick={handleCheckEligibility}
                loading={checkEligibilityMutation.isPending}
                disabled={!loanRequest.requestedAmount || !loanRequest.tenureMonths}
              >
                {checkEligibilityMutation.isPending ? 'Checking...' : 'Check Eligibility'}
              </Button>
              <Button
                variant="outline"
                onClick={() => setShowApplicationForm(false)}
              >
                Cancel
              </Button>
            </div>
          </div>
        </Card>
      )}

      {/* Loan History */}
      <Card>
        <CardHeader>
          <CardTitle>Loan Applications</CardTitle>
        </CardHeader>
        <div className="space-y-4">
          {loanHistory?.length > 0 ? (
            loanHistory.map((loan) => (
              <div
                key={loan.id}
                className="flex items-center justify-between p-4 border border-gray-200 rounded-lg hover:bg-gray-50"
              >
                <div className="flex items-center space-x-4">
                  <div className="w-12 h-12 bg-primary-100 rounded-full flex items-center justify-center">
                    <CreditCard className="h-6 w-6 text-primary-600" />
                  </div>
                  <div>
                    <p className="font-medium text-gray-900">
                      {loan.purpose?.replace('_', ' ').toUpperCase() || 'PERSONAL'} Loan
                    </p>
                    <p className="text-sm text-gray-500">
                      Applied on {formatDate(loan.createdAt)}
                    </p>
                  </div>
                </div>
                <div className="text-right">
                  <p className="font-semibold text-gray-900">
                    {formatCurrency(loan.requestedAmount)}
                  </p>
                  <div className="flex items-center justify-end mt-1">
                    {getStatusIcon(loan.status)}
                    <span className={`ml-2 text-sm font-medium ${getStatusColor(loan.status)}`}>
                      {loan.status}
                    </span>
                  </div>
                </div>
              </div>
            ))
          ) : (
            <div className="text-center py-8">
              <CreditCard className="h-12 w-12 text-gray-400 mx-auto mb-4" />
              <p className="text-gray-500">No loan applications yet</p>
              <Button
                className="mt-4"
                onClick={() => setShowApplicationForm(true)}
              >
                Apply for Your First Loan
              </Button>
            </div>
          )}
        </div>
      </Card>
    </div>
  )
}
