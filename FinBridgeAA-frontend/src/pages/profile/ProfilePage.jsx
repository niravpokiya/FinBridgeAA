import React, { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { User, Mail, Phone, Shield, Camera, Edit2, Check, X } from 'lucide-react'
import { useAuthStore } from '../../store/authStore'
import { userAPI } from '../../lib/api'
import { getInitials } from '../../lib/utils'
import Button from '../../components/ui/Button'
import Input from '../../components/ui/Input'
import Card from '../../components/ui/Card'
import CardHeader from '../../components/ui/Card'
import CardTitle from '../../components/ui/Card'
import LoadingSpinner from '../../components/ui/LoadingSpinner'

export default function ProfilePage() {
  const { user, updateUser } = useAuthStore()
  const [isEditing, setIsEditing] = useState(false)
  const [editForm, setEditForm] = useState({
    name: user?.name || '',
    email: user?.email || '',
    phone: user?.phone || ''
  })
  const [kycStatus, setKycStatus] = useState('pending') // pending, verified, rejected

  const queryClient = useQueryClient()

  const { data: profileData, isLoading } = useQuery({
    queryKey: ['userProfile'],
    queryFn: userAPI.getProfile,
    enabled: !!user,
  })

  const { data: kycData } = useQuery({
    queryKey: ['kycStatus'],
    queryFn: userAPI.getKycStatus,
    enabled: !!user,
  })

  const updateProfileMutation = useMutation({
    mutationFn: userAPI.updateProfile,
    onSuccess: (data) => {
      updateUser(data)
      setIsEditing(false)
      queryClient.invalidateQueries(['userProfile'])
    },
  })

  const handleEdit = () => {
    setEditForm({
      name: user?.name || '',
      email: user?.email || '',
      phone: user?.phone || ''
    })
    setIsEditing(true)
  }

  const handleSave = () => {
    updateProfileMutation.mutate(editForm)
  }

  const handleCancel = () => {
    setIsEditing(false)
    setEditForm({
      name: user?.name || '',
      email: user?.email || '',
      phone: user?.phone || ''
    })
  }

  const getKycStatusColor = (status) => {
    switch (status) {
      case 'verified':
        return 'bg-success-100 text-success-800'
      case 'pending':
        return 'bg-warning-100 text-warning-800'
      case 'rejected':
        return 'bg-error-100 text-error-800'
      default:
        return 'bg-gray-100 text-gray-800'
    }
  }

  const getKycStatusIcon = (status) => {
    switch (status) {
      case 'verified':
        return <Check className="h-4 w-4" />
      case 'pending':
        return <Clock className="h-4 w-4" />
      case 'rejected':
        return <X className="h-4 w-4" />
      default:
        return <Shield className="h-4 w-4" />
    }
  }

  if (isLoading) {
    return <LoadingSpinner />
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Profile</h1>
        <p className="text-gray-600 mt-1">Manage your personal information and account settings</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Profile Card */}
        <div className="lg:col-span-1">
          <Card>
            <div className="text-center">
              <div className="relative inline-block">
                <div className="w-24 h-24 bg-primary-600 rounded-full flex items-center justify-center mx-auto">
                  <span className="text-white text-3xl font-bold">
                    {getInitials(user?.name || 'User')}
                  </span>
                </div>
                <button className="absolute bottom-0 right-0 w-8 h-8 bg-white rounded-full shadow-md border border-gray-200 flex items-center justify-center hover:bg-gray-50">
                  <Camera className="h-4 w-4 text-gray-600" />
                </button>
              </div>
              
              <h2 className="mt-4 text-xl font-semibold text-gray-900">
                {user?.name || 'User'}
              </h2>
              <p className="text-gray-500">{user?.email}</p>
              
              <div className="mt-4">
                <span className={`inline-flex items-center px-3 py-1 rounded-full text-sm font-medium ${getKycStatusColor(kycStatus)}`}>
                  {getKycStatusIcon(kycStatus)}
                  <span className="ml-1">KYC {kycStatus?.toUpperCase()}</span>
                </span>
              </div>

              <div className="mt-6 space-y-2">
                <div className="flex justify-between text-sm">
                  <span className="text-gray-500">Member Since</span>
                  <span className="text-gray-900">Jan 2024</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-gray-500">Account Type</span>
                  <span className="text-gray-900">Premium</span>
                </div>
              </div>
            </div>
          </Card>
        </div>

        {/* Personal Information */}
        <div className="lg:col-span-2 space-y-6">
          <Card>
            <CardHeader className="flex flex-row items-center justify-between">
              <CardTitle>Personal Information</CardTitle>
              {!isEditing ? (
                <Button variant="outline" size="sm" onClick={handleEdit}>
                  <Edit2 className="h-4 w-4 mr-2" />
                  Edit
                </Button>
              ) : (
                <div className="flex space-x-2">
                  <Button size="sm" onClick={handleSave} loading={updateProfileMutation.isPending}>
                    <Check className="h-4 w-4 mr-2" />
                    Save
                  </Button>
                  <Button variant="outline" size="sm" onClick={handleCancel}>
                    <X className="h-4 w-4 mr-2" />
                    Cancel
                  </Button>
                </div>
              )}
            </CardHeader>

            <div className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Full Name
                  </label>
                  {isEditing ? (
                    <Input
                      value={editForm.name}
                      onChange={(e) => setEditForm(prev => ({ ...prev, name: e.target.value }))}
                      placeholder="Enter your full name"
                    />
                  ) : (
                    <div className="flex items-center space-x-2 p-3 bg-gray-50 rounded-lg">
                      <User className="h-4 w-4 text-gray-400" />
                      <span className="text-gray-900">{user?.name || 'Not provided'}</span>
                    </div>
                  )}
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Email Address
                  </label>
                  {isEditing ? (
                    <Input
                      type="email"
                      value={editForm.email}
                      onChange={(e) => setEditForm(prev => ({ ...prev, email: e.target.value }))}
                      placeholder="Enter your email"
                    />
                  ) : (
                    <div className="flex items-center space-x-2 p-3 bg-gray-50 rounded-lg">
                      <Mail className="h-4 w-4 text-gray-400" />
                      <span className="text-gray-900">{user?.email || 'Not provided'}</span>
                    </div>
                  )}
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Phone Number
                  </label>
                  {isEditing ? (
                    <Input
                      type="tel"
                      value={editForm.phone}
                      onChange={(e) => setEditForm(prev => ({ ...prev, phone: e.target.value }))}
                      placeholder="Enter your phone number"
                    />
                  ) : (
                    <div className="flex items-center space-x-2 p-3 bg-gray-50 rounded-lg">
                      <Phone className="h-4 w-4 text-gray-400" />
                      <span className="text-gray-900">{user?.phone || 'Not provided'}</span>
                    </div>
                  )}
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Date of Birth
                  </label>
                  <div className="flex items-center space-x-2 p-3 bg-gray-50 rounded-lg">
                    <span className="text-gray-900">Not provided</span>
                  </div>
                </div>
              </div>
            </div>
          </Card>

          {/* KYC Status */}
          <Card>
            <CardHeader>
              <CardTitle>KYC Verification</CardTitle>
            </CardHeader>

            <div className="space-y-4">
              <div className={`p-4 rounded-lg border ${getKycStatusColor(kycStatus)}`}>
                <div className="flex items-center space-x-3">
                  <Shield className="h-6 w-6" />
                  <div>
                    <h4 className="font-medium">KYC Status: {kycStatus?.toUpperCase()}</h4>
                    <p className="text-sm mt-1">
                      {kycStatus === 'verified' && 'Your identity has been successfully verified.'}
                      {kycStatus === 'pending' && 'Your KYC verification is in progress. This usually takes 1-2 business days.'}
                      {kycStatus === 'rejected' && 'Your KYC verification was rejected. Please resubmit with correct documents.'}
                    </p>
                  </div>
                </div>
              </div>

              {kycStatus !== 'verified' && (
                <Button className="w-full">
                  {kycStatus === 'rejected' ? 'Resubmit KYC' : 'Complete KYC Verification'}
                </Button>
              )}
            </div>
          </Card>

          {/* Security Settings */}
          <Card>
            <CardHeader>
              <CardTitle>Security Settings</CardTitle>
            </CardHeader>

            <div className="space-y-4">
              <div className="flex items-center justify-between p-4 border border-gray-200 rounded-lg">
                <div>
                  <h4 className="font-medium text-gray-900">Two-Factor Authentication</h4>
                  <p className="text-sm text-gray-500">Add an extra layer of security to your account</p>
                </div>
                <Button variant="outline" size="sm">Enable</Button>
              </div>

              <div className="flex items-center justify-between p-4 border border-gray-200 rounded-lg">
                <div>
                  <h4 className="font-medium text-gray-900">Change Password</h4>
                  <p className="text-sm text-gray-500">Update your account password</p>
                </div>
                <Button variant="outline" size="sm">Change</Button>
              </div>

              <div className="flex items-center justify-between p-4 border border-gray-200 rounded-lg">
                <div>
                  <h4 className="font-medium text-gray-900">Login History</h4>
                  <p className="text-sm text-gray-500">View recent login activity</p>
                </div>
                <Button variant="outline" size="sm">View</Button>
              </div>
            </div>
          </Card>
        </div>
      </div>
    </div>
  )
}
