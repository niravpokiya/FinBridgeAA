import React from 'react'
import { useQuery } from '@tanstack/react-query'
import { Shield, Clock, CheckCircle, AlertCircle } from 'lucide-react'
import { consentAPI } from '../../lib/api'
import Card from '../ui/Card'
import CardHeader from '../ui/Card'
import CardTitle from '../ui/Card'
import Button from '../ui/Button'
import LoadingSpinner from '../ui/LoadingSpinner'

export default function ConsentStatusCard() {
  const { data: consents, isLoading } = useQuery({
    queryKey: ['activeConsents'],
    queryFn: () => consentAPI.getActiveConsents('current-user-id'),
    refetchInterval: 30000, // Refresh every 30 seconds
  })

  if (isLoading) {
    return (
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center">
            <Shield className="w-5 h-5 mr-2" />
            Data Consents
          </CardTitle>
        </CardHeader>
        <div className="p-6">
          <LoadingSpinner />
        </div>
      </Card>
    )
  }

  const activeConsents = consents?.data || []
  const hasActiveConsents = activeConsents.length > 0

  return (
    <Card>
      <CardHeader>
        <CardTitle className="flex items-center justify-between">
          <div className="flex items-center">
            <Shield className="w-5 h-5 mr-2" />
            Data Consents
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
            <p className="text-gray-600 mb-4">
              Grant consent to access your financial data from banks and other institutions.
            </p>
            <Button className="w-full">
              Create New Consent
            </Button>
          </div>
        ) : (
          <div className="space-y-4">
            {activeConsents.map((consent) => (
              <div key={consent.id} className="border border-gray-200 rounded-lg p-4">
                <div className="flex items-start justify-between mb-2">
                  <div className="flex items-center">
                    <CheckCircle className="w-4 h-4 text-success-500 mr-2" />
                    <span className="font-medium text-gray-900">
                      {consent.fiName || 'Financial Institution'}
                    </span>
                  </div>
                  <span className={`text-xs px-2 py-1 rounded-full ${
                    consent.status === 'ACTIVE' 
                      ? 'bg-success-100 text-success-800'
                      : 'bg-gray-100 text-gray-800'
                  }`}>
                    {consent.status}
                  </span>
                </div>
                
                <div className="text-sm text-gray-600 space-y-1">
                  <div className="flex items-center">
                    <Clock className="w-3 h-3 mr-1" />
                    Expires: {new Date(consent.expiryDate).toLocaleDateString()}
                  </div>
                  <div>
                    Data Types: {consent.dataTypes?.join(', ') || 'Account data'}
                  </div>
                </div>
                
                <div className="mt-3 flex gap-2">
                  <Button size="sm" variant="outline">
                    View Details
                  </Button>
                  <Button size="sm" variant="ghost">
                    Revoke
                  </Button>
                </div>
              </div>
            ))}
            
            <Button variant="outline" className="w-full">
              + Add New Consent
            </Button>
          </div>
        )}
      </div>
    </Card>
  )
}
