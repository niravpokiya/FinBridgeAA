import React from 'react'
import { useQuery } from '@tanstack/react-query'
import { Shield, Clock, CheckCircle, X, AlertTriangle, Eye } from 'lucide-react'
import { consentAPI } from '../../lib/api'
import Card from '../ui/Card'
import CardHeader from '../ui/Card'
import CardTitle from '../ui/Card'
import Button from '../ui/Button'
import LoadingSpinner from '../ui/LoadingSpinner'

export default function ConsentRequestsCard() {
  // Mock data for demonstration - replace with actual API call when backend is ready
  const { data: consentRequests, isLoading } = useQuery({
    queryKey: ['consentRequests'],
    queryFn: async () => {
      // Simulate API delay
      await new Promise(resolve => setTimeout(resolve, 1000))
      return {
        data: [
          {
            id: 'REQ001',
            fiName: 'HDFC Bank',
            purpose: 'Loan application processing',
            dataTypes: ['Account Summary', 'Transaction History'],
            createdDate: new Date().toISOString(),
            expiryDate: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toISOString(), // 7 days from now
            status: 'PENDING'
          },
          {
            id: 'REQ002', 
            fiName: 'ICICI Bank',
            purpose: 'Credit card eligibility check',
            dataTypes: ['Account Summary', 'Monthly Statements'],
            createdDate: new Date().toISOString(),
            expiryDate: new Date(Date.now() + 5 * 24 * 60 * 60 * 1000).toISOString(), // 5 days from now
            status: 'PENDING'
          }
        ]
      }
    },
    refetchInterval: 30000, // Refresh every 30 seconds
  })

  const requests = consentRequests?.data || []
  const pendingRequests = requests.filter(r => r.status === 'PENDING')
  const hasPendingRequests = pendingRequests.length > 0

  if (isLoading) {
    return (
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center">
            <Shield className="w-5 h-5 mr-2" />
            Consent Requests
          </CardTitle>
        </CardHeader>
        <div className="p-6">
          <LoadingSpinner />
        </div>
      </Card>
    )
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle className="flex items-center justify-between">
          <div className="flex items-center">
            <Shield className="w-5 h-5 mr-2" />
            Consent Requests
          </div>
          {hasPendingRequests && (
            <span className="text-sm text-warning-600 font-medium">
              {pendingRequests.length} Pending
            </span>
          )}
        </CardTitle>
      </CardHeader>
      
      <div className="p-6">
        {!hasPendingRequests ? (
          <div className="text-center py-8">
            <CheckCircle className="w-12 h-12 text-gray-400 mx-auto mb-4" />
            <h3 className="text-lg font-medium text-gray-900 mb-2">No Pending Requests</h3>
            <p className="text-gray-600">
              You're all caught up! No consent requests waiting for your approval.
            </p>
          </div>
        ) : (
          <div className="space-y-4">
            {pendingRequests.map((request) => (
              <div key={request.id} className="border border-warning-200 bg-warning-50 rounded-lg p-4">
                <div className="flex items-start justify-between mb-3">
                  <div className="flex items-center">
                    <AlertTriangle className="w-4 h-4 text-warning-500 mr-2" />
                    <span className="font-medium text-gray-900">
                      {request.fiName || 'Financial Institution'}
                    </span>
                  </div>
                  <span className="text-xs px-2 py-1 bg-warning-100 text-warning-800 rounded-full">
                    PENDING
                  </span>
                </div>
                
                <div className="text-sm text-gray-700 space-y-2 mb-4">
                  <div>
                    <span className="font-medium">Purpose:</span> {request.purpose || 'Data access request'}
                  </div>
                  <div>
                    <span className="font-medium">Data Types:</span> {request.dataTypes?.join(', ') || 'Account information'}
                  </div>
                  <div className="flex items-center">
                    <Clock className="w-3 h-3 mr-1" />
                    Requested: {new Date(request.createdDate).toLocaleDateString()}
                  </div>
                  <div className="flex items-center">
                    <Clock className="w-3 h-3 mr-1" />
                    Expires: {new Date(request.expiryDate).toLocaleDateString()}
                  </div>
                </div>
                
                <div className="flex gap-2">
                  <Button 
                    size="sm" 
                    className="flex-1"
                    onClick={() => handleApprove(request.id)}
                  >
                    <CheckCircle className="w-4 h-4 mr-1" />
                    Approve
                  </Button>
                  <Button 
                    size="sm" 
                    variant="outline" 
                    className="flex-1"
                    onClick={() => handleReject(request.id)}
                  >
                    <X className="w-4 h-4 mr-1" />
                    Reject
                  </Button>
                  <Button 
                    size="sm" 
                    variant="ghost"
                    onClick={() => handleViewDetails(request)}
                  >
                    <Eye className="w-4 h-4" />
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

// These would be implemented with actual API calls
const handleApprove = async (consentId) => {
  console.log('Approving consent:', consentId)
  // Show success message
  alert(`Consent ${consentId} approved! (This is a demo - actual API call would go here)`)
}

const handleReject = async (consentId) => {
  console.log('Rejecting consent:', consentId)
  // Show success message
  alert(`Consent ${consentId} rejected! (This is a demo - actual API call would go here)`)
}

const handleViewDetails = (request) => {
  console.log('View details:', request)
  // Show detailed modal
  alert(`Consent Details:\n\nFI: ${request.fiName}\nPurpose: ${request.purpose}\nData Types: ${request.dataTypes?.join(', ')}\n\n(This would open a detailed modal)`)
}
