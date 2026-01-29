import React from 'react'
import { CreditCard, TrendingUp, FileText, Plus, ArrowRight } from 'lucide-react'
import Card from '../ui/Card'
import CardHeader from '../ui/Card'
import CardTitle from '../ui/Card'

export default function QuickActions() {
  const actions = [
    {
      title: 'Apply for Loan',
      description: 'Get instant approval',
      icon: CreditCard,
      color: 'bg-primary-100 text-primary-600',
      href: '/loans'
    },
    {
      title: 'Invest Now',
      description: 'Grow your wealth',
      icon: TrendingUp,
      color: 'bg-success-100 text-success-600',
      href: '/investments'
    },
    {
      title: 'View Reports',
      description: 'Financial insights',
      icon: FileText,
      color: 'bg-warning-100 text-warning-600',
      href: '/reports'
    },
    {
      title: 'Add Account',
      description: 'Connect new bank',
      icon: Plus,
      color: 'bg-purple-100 text-purple-600',
      href: '/accounts/add'
    }
  ]

  return (
    <Card>
      <CardHeader>
        <CardTitle>Quick Actions</CardTitle>
      </CardHeader>
      <div className="space-y-3">
        {actions.map((action, index) => (
          <a
            key={index}
            href={action.href}
            className="flex items-center justify-between p-3 rounded-lg hover:bg-gray-50 transition-colors group"
          >
            <div className="flex items-center space-x-3">
              <div className={`w-10 h-10 rounded-lg flex items-center justify-center ${action.color}`}>
                <action.icon className="h-5 w-5" />
              </div>
              <div>
                <p className="font-medium text-gray-900 group-hover:text-primary-600">
                  {action.title}
                </p>
                <p className="text-sm text-gray-500">{action.description}</p>
              </div>
            </div>
            <ArrowRight className="h-4 w-4 text-gray-400 group-hover:text-primary-600 transition-colors" />
          </a>
        ))}
      </div>
    </Card>
  )
}
