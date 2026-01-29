import React from 'react'
import { useQuery } from '@tanstack/react-query'
import { TrendingUp, TrendingDown, DollarSign, CreditCard, PiggyBank, BarChart3 } from 'lucide-react'
import { useAuthStore } from '../../store/authStore'
import { userAPI, loanAPI } from '../../lib/api'
import { formatCurrency, calculatePercentageChange } from '../../lib/utils'
import MetricCard from '../../components/ui/MetricCard'
import Card from '../../components/ui/Card'
import CardHeader from '../../components/ui/Card'
import CardTitle from '../../components/ui/Card'
import SpendingChart from '../../components/charts/SpendingChart'
import RecentTransactions from '../../components/dashboard/RecentTransactions'
import QuickActions from '../../components/dashboard/QuickActions'
import ConsentRequestsCard from '../../components/consent/ConsentRequestsCard'
import ActiveConsentsCard from '../../components/consent/ActiveConsentsCard'
import LoadingSpinner from '../../components/ui/LoadingSpinner'

export default function DashboardPage() {
  const { user } = useAuthStore()

  // Mock data for demonstration - replace with actual API calls
  const { data: userData, isLoading: userLoading } = useQuery({
    queryKey: ['userProfile'],
    queryFn: userAPI.getProfile,
    enabled: !!user,
  })

  const { data: loanHistory, isLoading: loansLoading } = useQuery({
    queryKey: ['loanHistory'],
    queryFn: loanAPI.getHistory,
    enabled: !!user,
  })

  // Mock dashboard data
  const dashboardData = {
    netWorth: 1250000,
    netWorthChange: 8.5,
    monthlySavings: 45000,
    monthlySavingsChange: 12.3,
    creditScore: 750,
    creditScoreChange: 15,
    investments: 350000,
    investmentsChange: 5.2,
    spending: [
      { category: 'Food & Dining', amount: 12000, percentage: 25 },
      { category: 'Transportation', amount: 8000, percentage: 17 },
      { category: 'Shopping', amount: 6000, percentage: 13 },
      { category: 'Entertainment', amount: 4000, percentage: 8 },
      { category: 'Bills & Utilities', amount: 15000, percentage: 31 },
      { category: 'Others', amount: 3000, percentage: 6 },
    ],
    recentTransactions: [
      { id: 1, description: 'Salary Credit', amount: 85000, type: 'credit', date: '2024-01-25' },
      { id: 2, description: 'Grocery Store', amount: -2500, type: 'debit', date: '2024-01-24' },
      { id: 3, description: 'Electric Bill', amount: -1200, type: 'debit', date: '2024-01-23' },
      { id: 4, description: 'Freelance Payment', amount: 15000, type: 'credit', date: '2024-01-22' },
      { id: 5, description: 'Restaurant', amount: -800, type: 'debit', date: '2024-01-21' },
    ]
  }

  if (userLoading || loansLoading) {
    return <LoadingSpinner />
  }

  return (
    <div className="space-y-6">
      {/* Welcome Section */}
      <div>
        <h1 className="text-3xl font-bold text-gray-900">
          Welcome back, {user?.name || 'User'}!
        </h1>
        <p className="text-gray-600 mt-1">Here's your financial overview for this month</p>
      </div>

      {/* Key Metrics */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <MetricCard
          title="Net Worth"
          value={dashboardData.netWorth}
          change={dashboardData.netWorthChange}
          icon={DollarSign}
          format="currency"
        />
        <MetricCard
          title="Monthly Savings"
          value={dashboardData.monthlySavings}
          change={dashboardData.monthlySavingsChange}
          icon={PiggyBank}
          format="currency"
        />
        <MetricCard
          title="Credit Score"
          value={dashboardData.creditScore}
          change={dashboardData.creditScoreChange}
          icon={CreditCard}
          format="number"
        />
        <MetricCard
          title="Investments"
          value={dashboardData.investments}
          change={dashboardData.investmentsChange}
          icon={BarChart3}
          format="currency"
        />
      </div>

      {/* Charts and Analytics */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Spending Chart */}
        <Card>
          <CardHeader>
            <CardTitle>Monthly Spending</CardTitle>
          </CardHeader>
          <SpendingChart data={dashboardData.spending} />
        </Card>

        {/* Account Overview */}
        <Card>
          <CardHeader>
            <CardTitle>Account Overview</CardTitle>
          </CardHeader>
          <div className="space-y-4">
            <div className="flex justify-between items-center p-3 bg-gray-50 rounded-lg">
              <div>
                <p className="font-medium text-gray-900">Primary Account</p>
                <p className="text-sm text-gray-500">****1234</p>
              </div>
              <div className="text-right">
                <p className="font-semibold text-gray-900">{formatCurrency(125000)}</p>
                <p className="text-sm text-success-600">+2.5%</p>
              </div>
            </div>
            <div className="flex justify-between items-center p-3 bg-gray-50 rounded-lg">
              <div>
                <p className="font-medium text-gray-900">Savings Account</p>
                <p className="text-sm text-gray-500">****5678</p>
              </div>
              <div className="text-right">
                <p className="font-semibold text-gray-900">{formatCurrency(450000)}</p>
                <p className="text-sm text-success-600">+1.8%</p>
              </div>
            </div>
            <div className="flex justify-between items-center p-3 bg-gray-50 rounded-lg">
              <div>
                <p className="font-medium text-gray-900">Investment Account</p>
                <p className="text-sm text-gray-500">****9012</p>
              </div>
              <div className="text-right">
                <p className="font-semibold text-gray-900">{formatCurrency(350000)}</p>
                <p className="text-sm text-success-600">+5.2%</p>
              </div>
            </div>
          </div>
        </Card>
      </div>

      {/* Recent Activity and Quick Actions */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Recent Transactions */}
        <div className="lg:col-span-2">
          <RecentTransactions transactions={dashboardData.recentTransactions} />
        </div>

        {/* Quick Actions */}
        <QuickActions />
      </div>

      {/* Consent Management */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Pending Consent Requests */}
        <ConsentRequestsCard />
        
        {/* Active Consents */}
        <ActiveConsentsCard />
      </div>

      {/* Financial Insights */}
      <Card>
        <CardHeader>
          <CardTitle>Financial Insights</CardTitle>
        </CardHeader>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="p-4 bg-blue-50 rounded-lg border border-blue-200">
            <div className="flex items-center mb-2">
              <TrendingUp className="w-5 h-5 text-blue-600 mr-2" />
              <h4 className="font-medium text-blue-900">Great Progress!</h4>
            </div>
            <p className="text-sm text-blue-700">
              Your savings increased by 12% this month. Keep it up!
            </p>
          </div>
          <div className="p-4 bg-yellow-50 rounded-lg border border-yellow-200">
            <div className="flex items-center mb-2">
              <TrendingDown className="w-5 h-5 text-yellow-600 mr-2" />
              <h4 className="font-medium text-yellow-900">Watch Spending</h4>
            </div>
            <p className="text-sm text-yellow-700">
              Dining expenses are 20% higher than last month.
            </p>
          </div>
          <div className="p-4 bg-green-50 rounded-lg border border-green-200">
            <div className="flex items-center mb-2">
              <DollarSign className="w-5 h-5 text-green-600 mr-2" />
              <h4 className="font-medium text-green-900">Investment Opportunity</h4>
            </div>
            <p className="text-sm text-green-700">
              You have surplus funds that could be invested for better returns.
            </p>
          </div>
        </div>
      </Card>
    </div>
  )
}
