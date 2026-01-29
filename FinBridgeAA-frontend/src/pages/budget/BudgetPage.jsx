import React, { useState } from 'react'
import { useQuery, useMutation } from '@tanstack/react-query'
import { PieChart, Pie, Cell, ResponsiveContainer, BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts'
import { PieChart as PieChartIcon, Plus, Target, TrendingUp, AlertTriangle } from 'lucide-react'
import { budgetAPI } from '../../lib/api'
import { formatCurrency, calculatePercentageChange } from '../../lib/utils'
import Button from '../../components/ui/Button'
import Card from '../../components/ui/Card'
import CardHeader from '../../components/ui/Card'
import CardTitle from '../../components/ui/Card'
import LoadingSpinner from '../../components/ui/LoadingSpinner'

const COLORS = ['#3b82f6', '#22c55e', '#f59e0b', '#ef4444', '#8b5cf6', '#6b7280']

export default function BudgetPage() {
  const [activeTab, setActiveTab] = useState('overview')
  const [showBudgetForm, setShowBudgetForm] = useState(false)

  // Mock data - replace with actual API calls
  const budgetData = {
    totalBudget: 50000,
    totalSpent: 32000,
    remaining: 18000,
    categories: [
      { name: 'Food & Dining', budget: 12000, spent: 8500, color: '#3b82f6' },
      { name: 'Transportation', budget: 8000, spent: 6500, color: '#22c55e' },
      { name: 'Shopping', budget: 10000, spent: 12000, color: '#ef4444' },
      { name: 'Entertainment', budget: 5000, spent: 3000, color: '#f59e0b' },
      { name: 'Bills & Utilities', budget: 15000, spent: 12000, color: '#8b5cf6' },
    ],
    monthlyTrend: [
      { month: 'Jan', budget: 45000, spent: 42000 },
      { month: 'Feb', budget: 45000, spent: 38000 },
      { month: 'Mar', budget: 50000, spent: 32000 },
      { month: 'Apr', budget: 50000, spent: 35000 },
      { month: 'May', budget: 50000, spent: 48000 },
      { month: 'Jun', budget: 50000, spent: 32000 },
    ],
    insights: [
      {
        type: 'warning',
        icon: AlertTriangle,
        title: 'Overspending Alert',
        description: 'You\'ve exceeded your shopping budget by 20% this month.'
      },
      {
        type: 'success',
        icon: TrendingUp,
        title: 'Great Progress!',
        description: 'Your food spending is 15% lower than last month.'
      },
      {
        type: 'info',
        icon: Target,
        title: 'Savings Opportunity',
        description: 'You can save ₹3,000 more by optimizing entertainment expenses.'
      }
    ]
  }

  const analyzeBudgetMutation = useMutation({
    mutationFn: budgetAPI.analyze,
    onSuccess: (data) => {
      console.log('Budget analysis result:', data)
    },
  })

  const getProgressColor = (spent, budget) => {
    const percentage = (spent / budget) * 100
    if (percentage >= 90) return 'bg-error-500'
    if (percentage >= 75) return 'bg-warning-500'
    return 'bg-success-500'
  }

  const getProgressTextColor = (spent, budget) => {
    const percentage = (spent / budget) * 100
    if (percentage >= 90) return 'text-error-600'
    if (percentage >= 75) return 'text-warning-600'
    return 'text-success-600'
  }

  const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
      return (
        <div className="bg-white p-3 rounded-lg shadow-lg border border-gray-200">
          <p className="font-medium text-gray-900">{label}</p>
          <p className="text-sm text-gray-600">
            Budget: {formatCurrency(payload[0].payload.budget)}
          </p>
          <p className="text-sm text-gray-600">
            Spent: {formatCurrency(payload[0].payload.spent)}
          </p>
        </div>
      )
    }
    return null
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Budget & Expenses</h1>
          <p className="text-gray-600 mt-1">Track your spending and manage your budget</p>
        </div>
        <Button onClick={() => setShowBudgetForm(!showBudgetForm)}>
          <Plus className="h-4 w-4 mr-2" />
          Create Budget
        </Button>
      </div>

      {/* Budget Overview Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card>
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600">Total Budget</p>
              <p className="text-2xl font-bold text-gray-900">
                {formatCurrency(budgetData.totalBudget)}
              </p>
            </div>
            <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center">
              <Target className="h-6 w-6 text-blue-600" />
            </div>
          </div>
        </Card>

        <Card>
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600">Total Spent</p>
              <p className="text-2xl font-bold text-gray-900">
                {formatCurrency(budgetData.totalSpent)}
              </p>
              <p className="text-sm text-error-600">
                {((budgetData.totalSpent / budgetData.totalBudget) * 100).toFixed(1)}% used
              </p>
            </div>
            <div className="w-12 h-12 bg-red-100 rounded-full flex items-center justify-center">
              <TrendingUp className="h-6 w-6 text-red-600" />
            </div>
          </div>
        </Card>

        <Card>
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600">Remaining</p>
              <p className="text-2xl font-bold text-gray-900">
                {formatCurrency(budgetData.remaining)}
              </p>
              <p className="text-sm text-success-600">
                {((budgetData.remaining / budgetData.totalBudget) * 100).toFixed(1)}% left
              </p>
            </div>
            <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center">
              <PieChartIcon className="h-6 w-6 text-green-600" />
            </div>
          </div>
        </Card>
      </div>

      {/* Tabs */}
      <div className="border-b border-gray-200">
        <nav className="-mb-px flex space-x-8">
          {['overview', 'categories', 'trends'].map((tab) => (
            <button
              key={tab}
              onClick={() => setActiveTab(tab)}
              className={`
                py-2 px-1 border-b-2 font-medium text-sm capitalize
                ${activeTab === tab
                  ? 'border-primary-500 text-primary-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                }
              `}
            >
              {tab}
            </button>
          ))}
        </nav>
      </div>

      {/* Tab Content */}
      {activeTab === 'overview' && (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Category Breakdown */}
          <Card>
            <CardHeader>
              <CardTitle>Category Breakdown</CardTitle>
            </CardHeader>
            <div className="h-80">
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie
                    data={budgetData.categories}
                    cx="50%"
                    cy="50%"
                    outerRadius={80}
                    fill="#8884d8"
                    dataKey="spent"
                  >
                    {budgetData.categories.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={entry.color} />
                    ))}
                  </Pie>
                  <Tooltip content={<CustomTooltip />} />
                  <Legend />
                </PieChart>
              </ResponsiveContainer>
            </div>
          </Card>

          {/* Budget Insights */}
          <Card>
            <CardHeader>
              <CardTitle>Budget Insights</CardTitle>
            </CardHeader>
            <div className="space-y-4">
              {budgetData.insights.map((insight, index) => (
                <div
                  key={index}
                  className={`
                    p-4 rounded-lg border
                    ${insight.type === 'warning' ? 'bg-warning-50 border-warning-200' : ''}
                    ${insight.type === 'success' ? 'bg-success-50 border-success-200' : ''}
                    ${insight.type === 'info' ? 'bg-blue-50 border-blue-200' : ''}
                  `}
                >
                  <div className="flex items-start space-x-3">
                    <insight.icon
                      className={`
                        h-5 w-5 mt-0.5
                        ${insight.type === 'warning' ? 'text-warning-600' : ''}
                        ${insight.type === 'success' ? 'text-success-600' : ''}
                        ${insight.type === 'info' ? 'text-blue-600' : ''}
                      `}
                    />
                    <div>
                      <h4 className="font-medium text-gray-900">{insight.title}</h4>
                      <p className="text-sm text-gray-600 mt-1">{insight.description}</p>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </Card>
        </div>
      )}

      {activeTab === 'categories' && (
        <Card>
          <CardHeader>
            <CardTitle>Category Budgets</CardTitle>
          </CardHeader>
          <div className="space-y-6">
            {budgetData.categories.map((category) => {
              const percentage = (category.spent / category.budget) * 100
              const isOverBudget = percentage > 100

              return (
                <div key={category.name} className="space-y-2">
                  <div className="flex justify-between items-center">
                    <div className="flex items-center space-x-3">
                      <div
                        className="w-4 h-4 rounded-full"
                        style={{ backgroundColor: category.color }}
                      />
                      <span className="font-medium text-gray-900">{category.name}</span>
                    </div>
                    <div className="text-right">
                      <p className="font-medium text-gray-900">
                        {formatCurrency(category.spent)} / {formatCurrency(category.budget)}
                      </p>
                      <p className={`text-sm ${getProgressTextColor(category.spent, category.budget)}`}>
                        {percentage.toFixed(1)}% used
                        {isOverBudget && ' (Over budget!)'}
                      </p>
                    </div>
                  </div>
                  <div className="w-full bg-gray-200 rounded-full h-2">
                    <div
                      className={`h-2 rounded-full transition-all duration-300 ${getProgressColor(category.spent, category.budget)}`}
                      style={{ width: `${Math.min(percentage, 100)}%` }}
                    />
                  </div>
                </div>
              )
            })}
          </div>
        </Card>
      )}

      {activeTab === 'trends' && (
        <Card>
          <CardHeader>
            <CardTitle>Monthly Spending Trends</CardTitle>
          </CardHeader>
          <div className="h-80">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={budgetData.monthlyTrend}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="month" />
                <YAxis />
                <Tooltip content={<CustomTooltip />} />
                <Legend />
                <Bar dataKey="budget" fill="#3b82f6" name="Budget" />
                <Bar dataKey="spent" fill="#22c55e" name="Spent" />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </Card>
      )}
    </div>
  )
}
