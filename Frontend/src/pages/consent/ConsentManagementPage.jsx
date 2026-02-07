import React, { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { useLocation } from 'react-router-dom'
import { Shield, Plus, Search, Filter, CheckCircle, Clock, AlertCircle, X } from 'lucide-react'
import { consentAPI } from '../../lib/api'
import { useAuthStore } from '../../store/authStore'
import Card from '../../components/ui/Card'
import CardHeader from '../../components/ui/Card'
import CardTitle from '../../components/ui/Card'
import Button from '../../components/ui/Button'
import LoadingSpinner from '../../components/ui/LoadingSpinner'
import CreateConsentModal from '../../components/consent/CreateConsentModal'

export default function ConsentManagementPage() {
  const location = useLocation()
  const { user } = useAuthStore()
  const [showCreateModal, setShowCreateModal] = useState(false)
  const [searchTerm, setSearchTerm] = useState('')
  const [filterStatus, setFilterStatus] = useState('all')
  
  const queryClient = useQueryClient()

  const { data: consents, isLoading, error } = useQuery({
    queryKey: ['userConsents', filterStatus],
    queryFn: () => consentAPI.getActiveConsent(user?.id || 'current-user-id'),
    enabled: !!user && location.pathname === '/consents', // Only fetch when on consents page
    retry: 1,
  })

  // Debug logging
  React.useEffect(() => {
    console.log('Consents data:', consents)
    console.log('Consents error:', error)
    console.log('User data:', user)
    console.log('Current path:', location.pathname)
  }, [consents, error, user, location.pathname])

  const revokeConsentMutation = useMutation({
    mutationFn: consentAPI.declineConsent,
    onSuccess: () => {
      queryClient.invalidateQueries(['userConsents'])
    },
  })

  const handleRevokeConsent = (consentId) => {
    if (window.confirm('Are you sure you want to revoke this consent?')) {
      revokeConsentMutation.mutate(consentId)
    }
  }

  const filteredConsents = consents?.data?.filter(consent => {
    const matchesSearch = consent.fiName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         consent.id?.toLowerCase().includes(searchTerm.toLowerCase())
    const matchesFilter = filterStatus === 'all' || consent.status === filterStatus
    return matchesSearch && matchesFilter
  }) || 
  // Fallback sample data when API fails or returns no data
  (error ? [
    {
      id: 'SAMPLE-001',
      fiName: 'HDFC Bank',
      status: 'ACTIVE',
      createdDate: '2024-01-15',
      expiryDate: '2024-04-15',
      dataTypes: ['Account Balance', 'Transaction History']
    },
    {
      id: 'SAMPLE-002', 
      fiName: 'ICICI Bank',
      status: 'EXPIRED',
      createdDate: '2023-12-01',
      expiryDate: '2024-01-01',
      dataTypes: ['Account Balance']
    }
  ] : [])

  const getStatusIcon = (status) => {
    switch (status) {
      case 'ACTIVE':
        return <CheckCircle className="w-4 h-4 text-success-500" />
      case 'EXPIRED':
        return <Clock className="w-4 h-4 text-warning-500" />
      case 'REVOKED':
        return <X className="w-4 h-4 text-error-500" />
      default:
        return <AlertCircle className="w-4 h-4 text-gray-500" />
    }
  }

  const getStatusColor = (status) => {
    switch (status) {
      case 'ACTIVE':
        return 'bg-success-100 text-success-800'
      case 'EXPIRED':
        return 'bg-warning-100 text-warning-800'
      case 'REVOKED':
        return 'bg-error-100 text-error-800'
      default:
        return 'bg-gray-100 text-gray-800'
    }
  }

  if (isLoading) {
    return (
      <div className="flex items-center justify-center p-6">
        <LoadingSpinner size="lg" />
      </div>
    )
  }

  if (error) {
    return (
      <div className="p-6">
        <div className="max-w-7xl mx-auto">
          <div className="bg-error-50 border border-error-200 rounded-lg p-6">
            <div className="flex items-center">
              <AlertCircle className="w-5 h-5 text-error-600 mr-2" />
              <h3 className="text-lg font-medium text-error-900">Error loading consents</h3>
            </div>
            <p className="text-error-700 mt-2">
              {error.message || 'Failed to load consent data. Please try again later.'}
            </p>
            <Button 
              onClick={() => queryClient.invalidateQueries(['userConsents'])}
              className="mt-4"
              variant="outline"
            >
              Retry
            </Button>
          </div>
        </div>
      </div>
    )
  }

  return (
    <div className="p-6">
      <div className="max-w-7xl mx-auto">
        {/* Header */}
        <div className="mb-8">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold text-gray-900">Consent Management</h1>
              <p className="text-gray-600 mt-2">
                Manage your data sharing consents with financial institutions
              </p>
            </div>
            <Button onClick={() => setShowCreateModal(true)}>
              <Plus className="w-4 h-4 mr-2" />
              Create New Consent
            </Button>
          </div>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
          <Card>
            <div className="p-6">
              <div className="flex items-center">
                <div className="p-3 bg-success-100 rounded-lg">
                  <CheckCircle className="w-6 h-6 text-success-600" />
                </div>
                <div className="ml-4">
                  <p className="text-sm text-gray-600">Active Consents</p>
                  <p className="text-2xl font-bold text-gray-900">
                    {filteredConsents.filter(c => c.status === 'ACTIVE').length}
                  </p>
                </div>
              </div>
            </div>
          </Card>

          <Card>
            <div className="p-6">
              <div className="flex items-center">
                <div className="p-3 bg-warning-100 rounded-lg">
                  <Clock className="w-6 h-6 text-warning-600" />
                </div>
                <div className="ml-4">
                  <p className="text-sm text-gray-600">Expiring Soon</p>
                  <p className="text-2xl font-bold text-gray-900">
                    {filteredConsents.filter(c => {
                      const expiryDate = new Date(c.expiryDate)
                      const sevenDaysFromNow = new Date()
                      sevenDaysFromNow.setDate(sevenDaysFromNow.getDate() + 7)
                      return c.status === 'ACTIVE' && expiryDate <= sevenDaysFromNow
                    }).length}
                  </p>
                </div>
              </div>
            </div>
          </Card>

          <Card>
            <div className="p-6">
              <div className="flex items-center">
                <div className="p-3 bg-error-100 rounded-lg">
                  <X className="w-6 h-6 text-error-600" />
                </div>
                <div className="ml-4">
                  <p className="text-sm text-gray-600">Revoked</p>
                  <p className="text-2xl font-bold text-gray-900">
                    {filteredConsents.filter(c => c.status === 'REVOKED').length}
                  </p>
                </div>
              </div>
            </div>
          </Card>

          <Card>
            <div className="p-6">
              <div className="flex items-center">
                <div className="p-3 bg-primary-100 rounded-lg">
                  <Shield className="w-6 h-6 text-primary-600" />
                </div>
                <div className="ml-4">
                  <p className="text-sm text-gray-600">Total Consents</p>
                  <p className="text-2xl font-bold text-gray-900">
                    {filteredConsents.length}
                  </p>
                </div>
              </div>
            </div>
          </Card>
        </div>

        {/* Search and Filter */}
        <Card className="mb-6">
          <div className="p-6">
            <div className="flex flex-col md:flex-row gap-4">
              <div className="flex-1">
                <div className="relative">
                  <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-400" />
                  <input
                    type="text"
                    placeholder="Search by institution name or consent ID..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="pl-10 w-full input"
                  />
                </div>
              </div>
              <div className="flex gap-2">
                <select
                  value={filterStatus}
                  onChange={(e) => setFilterStatus(e.target.value)}
                  className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
                >
                  <option value="all">All Status</option>
                  <option value="ACTIVE">Active</option>
                  <option value="EXPIRED">Expired</option>
                  <option value="REVOKED">Revoked</option>
                </select>
              </div>
            </div>
          </div>
        </Card>

        {/* Consents List */}
        <Card>
          <CardHeader>
            <CardTitle>Your Consents</CardTitle>
          </CardHeader>
          <div className="p-6">
            {filteredConsents.length === 0 ? (
              <div className="text-center py-12">
                <Shield className="w-16 h-16 text-gray-400 mx-auto mb-4" />
                <h3 className="text-lg font-medium text-gray-900 mb-2">No consents found</h3>
                <p className="text-gray-600 mb-4">
                  {searchTerm || filterStatus !== 'all' 
                    ? 'Try adjusting your search or filter criteria'
                    : 'Create your first consent to start sharing your financial data'
                  }
                </p>
                {!searchTerm && filterStatus === 'all' && (
                  <Button onClick={() => setShowCreateModal(true)}>
                    <Plus className="w-4 h-4 mr-2" />
                    Create Your First Consent
                  </Button>
                )}
              </div>
            ) : (
              <div className="space-y-4">
                {filteredConsents.map((consent) => (
                  <div key={consent.id} className="border border-gray-200 rounded-lg p-6 hover:shadow-md transition-shadow">
                    <div className="flex items-start justify-between">
                      <div className="flex-1">
                        <div className="flex items-center mb-2">
                          {getStatusIcon(consent.status)}
                          <h3 className="text-lg font-medium text-gray-900 ml-2">
                            {consent.fiName || 'Financial Institution'}
                          </h3>
                          <span className={`ml-3 text-xs px-2 py-1 rounded-full ${getStatusColor(consent.status)}`}>
                            {consent.status}
                          </span>
                        </div>
                        
                        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm text-gray-600">
                          <div>
                            <span className="font-medium">Consent ID:</span> {consent.id}
                          </div>
                          <div>
                            <span className="font-medium">Created:</span> {new Date(consent.createdDate).toLocaleDateString()}
                          </div>
                          <div>
                            <span className="font-medium">Expires:</span> {new Date(consent.expiryDate).toLocaleDateString()}
                          </div>
                        </div>
                        
                        <div className="mt-3">
                          <span className="font-medium text-sm text-gray-700">Data Types:</span>
                          <div className="flex flex-wrap gap-2 mt-1">
                            {consent.dataTypes?.map((type, index) => (
                              <span key={index} className="text-xs px-2 py-1 bg-gray-100 text-gray-700 rounded">
                                {type}
                              </span>
                            )) || <span className="text-sm text-gray-500">Account data</span>}
                          </div>
                        </div>
                      </div>
                      
                      <div className="ml-4 flex flex-col gap-2">
                        <Button size="sm" variant="outline">
                          View Details
                        </Button>
                        {consent.status === 'ACTIVE' && (
                          <Button 
                            size="sm" 
                            variant="ghost"
                            onClick={() => handleRevokeConsent(consent.id)}
                            disabled={revokeConsentMutation.isLoading}
                          >
                            Revoke
                          </Button>
                        )}
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </Card>

        {/* Create Consent Modal */}
        {showCreateModal && (
          <CreateConsentModal
            onClose={() => setShowCreateModal(false)}
            onSuccess={() => {
              setShowCreateModal(false)
              queryClient.invalidateQueries(['userConsents'])
            }}
          />
        )}
      </div>
    </div>
  )
}
