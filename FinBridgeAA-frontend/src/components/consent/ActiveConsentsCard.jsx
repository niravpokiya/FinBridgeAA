import React from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { Shield, CheckCircle, Clock, X, AlertCircle } from 'lucide-react'
import { consentAPI } from '../../lib/api'
import Card from '../ui/Card'
import CardHeader from '../ui/Card'
import CardTitle from '../ui/Card'
import Button from '../ui/Button'
import LoadingSpinner from '../ui/LoadingSpinner'

export default function ActiveConsentsCard() {
  const queryClient = useQueryClient()

  // Mock data for demonstration - replace with actual API call when backend is ready
  const { data: consents, isLoading } = useQuery({
    queryKey: ['activeConsents'],
    queryFn: async () => {
      // Simulate API delay
      await new Promise(resolve => setTimeout(resolve, 1000))
      return {
        data: [
          {
            id: 'CONS001',
            fiName: 'State Bank of India',
            purpose: 'Account verification',
            dataTypes: ['Account Summary', 'Balance Information'],
            approvedDate: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000).toISOString(), // 2 days ago
            expiryDate: new Date(Date.now() + 5 * 24 * 60 * 60 * 1000).toISOString(), // 5 days from now
            status: 'ACTIVE'
          },
          {
            id: 'CONS002',
            fiName: 'Axis Bank',
            purpose: 'Investment portfolio analysis',
            dataTypes: ['Investment Details', 'Transaction History'],
            approvedDate: new Date(Date.now() - 5 * 24 * 60 * 60 * 1000).toISOString(), // 5 days ago
            expiryDate: new Date(Date.now() + 10 * 24 * 60 * 60 * 1000).toISOString(), // 10 days from now
            status: 'ACTIVE'
          }
        ]
      }
    },
    refetchInterval: 30000, // Refresh every 30 seconds
  })

  const revokeConsentMutation = useMutation({
    mutationFn: consentAPI.declineConsent,
    onSuccess: () => {
      queryClient.invalidateQueries(['activeConsents'])
    },
  })

  const handleRevokeConsent = (consentId) => {
    if (window.confirm('Are you sure you want to revoke this consent? This will stop data access immediately.')) {
      revokeConsentMutation.mutate(consentId)
      alert(`Consent ${consentId} revoked! (This is a demo - actual API call would go here)`)
    }
  }

  if (isLoading) {
    return (
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center">
            <Shield className="w-5 h-5 mr-2" />
            Active Consents
          </CardTitle>
        </CardHeader>
        <div className="p-6">
          <LoadingSpinner />
        </div>
      </Card>
    )
  }

  const activeConsents = consents?.data?.filter(c => c.status === 'ACTIVE') || []
  const hasActiveConsents = activeConsents.length > 0

  return (
    <Card>
      <CardHeader>
        <CardTitle className="flex items-center justify-between">
          <div className="flex items-center">
            <Shield className="w-5 h-5 mr-2" />
            Active Consents
          </div>
          {hasActiveConsents && (
            <span className="text-sm text-success-600 font-medium">
              {activeConsents.length} Active
            </span>
          )}
        </CardTitle>
      </CardHeader>
      
      <div className="p-6">
        {!hasActiveConsents ? (
          <div className="text-center py-8">
            <AlertCircle className="w-12 h-12 text-gray-400 mx-auto mb-4" />
            <h3 className="text-lg font-medium text-gray-900 mb-2">No Active Consents</h3>
            <p className="text-gray-600">
              You haven't approved any consent requests yet.
            </p>
          </div>
        ) : (
          <div className="space-y-4">
            {activeConsents.map((consent) => (
              <div key={consent.id} className="border border-success-200 bg-success-50 rounded-lg p-4">
                <div className="flex items-start justify-between mb-3">
                  <div className="flex items-center">
                    <CheckCircle className="w-4 h-4 text-success-500 mr-2" />
                    <span className="font-medium text-gray-900">
                      {consent.fiName || 'Financial Institution'}
                    </span>
                  </div>
                  <span className="text-xs px-2 py-1 bg-success-100 text-success-800 rounded-full">
                    ACTIVE
                  </span>
                </div>
                
                <div className="text-sm text-gray-700 space-y-2 mb-4">
                  <div>
                    <span className="font-medium">Purpose:</span> {consent.purpose || 'Data access'}
                  </div>
                  <div>
                    <span className="font-medium">Data Types:</span> {consent.dataTypes?.join(', ') || 'Account information'}
                  </div>
                  <div className="flex items-center">
                    <Clock className="w-3 h-3 mr-1" />
                    Approved: {new Date(consent.approvedDate).toLocaleDateString()}
                  </div>
                  <div className="flex items-center">
                    <Clock className="w-3 h-3 mr-1" />
                    Expires: {new Date(consent.expiryDate).toLocaleDateString()}
                  </div>
                </div>
                
                <div className="flex gap-2">
                  <Button 
                    size="sm" 
                    variant="outline"
                    onClick={() => console.log('View details:', consent)}
                  >
                    View Details
                  </Button>
                  <Button 
                    size="sm" 
                    variant="ghost"
                    onClick={() => handleRevokeConsent(consent.id)}
                    disabled={revokeConsentMutation.isLoading}
                  >
                    <X className="w-4 h-4 mr-1" />
                    Revoke
                  </Button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </Card>
  )
}
