import React, { useState } from 'react'
import { useMutation, useQuery } from '@tanstack/react-query'
import { X, Calendar, Shield, Banknote, CreditCard, FileText, TrendingUp } from 'lucide-react'
import { consentAPI, aaAPI } from '../../lib/api'
import { useAuthStore } from '../../store/authStore'
import Card from '../ui/Card'
import CardHeader from '../ui/Card'
import CardTitle from '../ui/Card'
import Button from '../ui/Button'
import Input from '../ui/Input'
import LoadingSpinner from '../ui/LoadingSpinner'

const DATA_TYPES = [
  { id: 'ACCOUNT_SUMMARY', name: 'Account Summary', icon: Banknote, description: 'Basic account information and balances' },
  { id: 'TRANSACTIONS', name: 'Transactions', icon: CreditCard, description: 'Transaction history and details' },
  { id: 'STATEMENTS', name: 'Statements', icon: FileText, description: 'Monthly/quarterly statements' },
  { id: 'INVESTMENTS', name: 'Investments', icon: TrendingUp, description: 'Investment portfolio and holdings' },
]

const FINANCIAL_INSTITUTIONS = [
  { id: 'hdfc', name: 'HDFC Bank', logo: '🏦' },
  { id: 'icici', name: 'ICICI Bank', logo: '🏦' },
  { id: 'sbi', name: 'State Bank of India', logo: '🏦' },
  { id: 'axis', name: 'Axis Bank', logo: '🏦' },
  { id: 'kotak', name: 'Kotak Mahindra Bank', logo: '🏦' },
]

export default function CreateConsentModal({ onClose, onSuccess }) {
  const { user } = useAuthStore()
  const [selectedFI, setSelectedFI] = useState('')
  const [selectedDataTypes, setSelectedDataTypes] = useState([])
  const [expiryDate, setExpiryDate] = useState('')
  const [purpose, setPurpose] = useState('')
  const [step, setStep] = useState(1)

  const createConsentMutation = useMutation({
    mutationFn: consentAPI.createConsent,
    onSuccess: () => {
      onSuccess()
    },
  })

  const handleDataTypeToggle = (dataTypeId) => {
    setSelectedDataTypes(prev => 
      prev.includes(dataTypeId) 
        ? prev.filter(id => id !== dataTypeId)
        : [...prev, dataTypeId]
    )
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    
    const consentData = {
      userId: user?.id,
      fiId: selectedFI,
      dataTypes: selectedDataTypes,
      expiryDate,
      purpose,
    }

    createConsentMutation.mutate(consentData)
  }

  const canProceed = () => {
    if (step === 1) return selectedFI
    if (step === 2) return selectedDataTypes.length > 0
    if (step === 3) return expiryDate && purpose
    return false
  }

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <Card className="w-full max-w-2xl max-h-[90vh] overflow-y-auto">
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle className="flex items-center">
              <Shield className="w-5 h-5 mr-2" />
              Create New Consent
            </CardTitle>
            <Button variant="ghost" size="sm" onClick={onClose}>
              <X className="w-4 h-4" />
            </Button>
          </div>
          
          {/* Progress Steps */}
          <div className="flex items-center justify-between mt-4">
            {[1, 2, 3].map((stepNumber) => (
              <div key={stepNumber} className="flex items-center">
                <div className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-medium ${
                  step >= stepNumber 
                    ? 'bg-primary-600 text-white' 
                    : 'bg-gray-200 text-gray-600'
                }`}>
                  {stepNumber}
                </div>
                {stepNumber < 3 && (
                  <div className={`w-16 h-1 mx-2 ${
                    step > stepNumber ? 'bg-primary-600' : 'bg-gray-200'
                  }`} />
                )}
              </div>
            ))}
          </div>
        </CardHeader>

        <div className="p-6">
          {step === 1 && (
            <div>
              <h3 className="text-lg font-medium text-gray-900 mb-4">Select Financial Institution</h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {FINANCIAL_INSTITUTIONS.map((fi) => (
                  <div
                    key={fi.id}
                    onClick={() => setSelectedFI(fi.id)}
                    className={`border-2 rounded-lg p-4 cursor-pointer transition-colors ${
                      selectedFI === fi.id
                        ? 'border-primary-500 bg-primary-50'
                        : 'border-gray-200 hover:border-gray-300'
                    }`}
                  >
                    <div className="flex items-center">
                      <span className="text-2xl mr-3">{fi.logo}</span>
                      <span className="font-medium">{fi.name}</span>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {step === 2 && (
            <div>
              <h3 className="text-lg font-medium text-gray-900 mb-4">Select Data Types</h3>
              <div className="space-y-3">
                {DATA_TYPES.map((dataType) => {
                  const Icon = dataType.icon
                  const isSelected = selectedDataTypes.includes(dataType.id)
                  
                  return (
                    <div
                      key={dataType.id}
                      onClick={() => handleDataTypeToggle(dataType.id)}
                      className={`border-2 rounded-lg p-4 cursor-pointer transition-colors ${
                        isSelected
                          ? 'border-primary-500 bg-primary-50'
                          : 'border-gray-200 hover:border-gray-300'
                      }`}
                    >
                      <div className="flex items-start">
                        <div className="flex items-center mr-3">
                          <input
                            type="checkbox"
                            checked={isSelected}
                            onChange={() => handleDataTypeToggle(dataType.id)}
                            className="w-4 h-4 text-primary-600 border-gray-300 rounded focus:ring-primary-500"
                          />
                        </div>
                        <div className="flex-1">
                          <div className="flex items-center">
                            <Icon className="w-5 h-5 mr-2 text-gray-600" />
                            <span className="font-medium">{dataType.name}</span>
                          </div>
                          <p className="text-sm text-gray-600 mt-1">{dataType.description}</p>
                        </div>
                      </div>
                    </div>
                  )
                })}
              </div>
            </div>
          )}

          {step === 3 && (
            <div>
              <h3 className="text-lg font-medium text-gray-900 mb-4">Consent Details</h3>
              <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                  <Input
                    label="Expiry Date"
                    type="date"
                    value={expiryDate}
                    onChange={(e) => setExpiryDate(e.target.value)}
                    min={new Date().toISOString().split('T')[0]}
                    required
                  />
                </div>
                
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Purpose of Data Access
                  </label>
                  <textarea
                    value={purpose}
                    onChange={(e) => setPurpose(e.target.value)}
                    rows={3}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
                    placeholder="e.g., For loan application processing, financial analysis, etc."
                    required
                  />
                </div>

                <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
                  <div className="flex items-start">
                    <Shield className="w-5 h-5 text-yellow-600 mr-2 mt-0.5" />
                    <div>
                      <h4 className="font-medium text-yellow-800">Consent Summary</h4>
                      <ul className="text-sm text-yellow-700 mt-2 space-y-1">
                        <li>• Institution: {FINANCIAL_INSTITUTIONS.find(fi => fi.id === selectedFI)?.name}</li>
                        <li>• Data Types: {selectedDataTypes.length} selected</li>
                        <li>• Expires: {expiryDate ? new Date(expiryDate).toLocaleDateString() : 'Not set'}</li>
                        <li>• You can revoke this consent at any time</li>
                      </ul>
                    </div>
                  </div>
                </div>
              </form>
            </div>
          )}

          {/* Action Buttons */}
          <div className="flex justify-between mt-6">
            <div>
              {step > 1 && (
                <Button variant="outline" onClick={() => setStep(step - 1)}>
                  Previous
                </Button>
              )}
            </div>
            
            <div className="flex gap-2">
              <Button variant="outline" onClick={onClose}>
                Cancel
              </Button>
              
              {step < 3 ? (
                <Button onClick={() => setStep(step + 1)} disabled={!canProceed()}>
                  Next
                </Button>
              ) : (
                <Button 
                  onClick={handleSubmit} 
                  disabled={!canProceed || createConsentMutation.isLoading}
                >
                  {createConsentMutation.isLoading ? (
                    <>
                      <LoadingSpinner size="sm" className="mr-2" />
                      Creating...
                    </>
                  ) : (
                    'Create Consent'
                  )}
                </Button>
              )}
            </div>
          </div>
        </div>
      </Card>
    </div>
  )
}
