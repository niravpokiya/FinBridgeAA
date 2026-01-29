import React from 'react'
import { ArrowUpRight, ArrowDownRight, MoreHorizontal } from 'lucide-react'
import { formatCurrency, formatDate } from '../../lib/utils'
import Card from '../ui/Card'
import CardHeader from '../ui/Card'
import CardTitle from '../ui/Card'

export default function RecentTransactions({ transactions }) {
  return (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between">
        <CardTitle>Recent Transactions</CardTitle>
        <button className="text-gray-400 hover:text-gray-600">
          <MoreHorizontal className="h-5 w-5" />
        </button>
      </CardHeader>
      <div className="space-y-4">
        {transactions.map((transaction) => (
          <div
            key={transaction.id}
            className="flex items-center justify-between p-3 hover:bg-gray-50 rounded-lg transition-colors"
          >
            <div className="flex items-center space-x-3">
              <div
                className={`
                  w-10 h-10 rounded-full flex items-center justify-center
                  ${transaction.type === 'credit' 
                    ? 'bg-success-100 text-success-600' 
                    : 'bg-error-100 text-error-600'
                  }
                `}
              >
                {transaction.type === 'credit' ? (
                  <ArrowUpRight className="h-5 w-5" />
                ) : (
                  <ArrowDownRight className="h-5 w-5" />
                )}
              </div>
              <div>
                <p className="font-medium text-gray-900">{transaction.description}</p>
                <p className="text-sm text-gray-500">{formatDate(transaction.date)}</p>
              </div>
            </div>
            <div className="text-right">
              <p
                className={`
                  font-semibold
                  ${transaction.type === 'credit' ? 'text-success-600' : 'text-error-600'}
                `}
              >
                {transaction.type === 'credit' ? '+' : ''}
                {formatCurrency(Math.abs(transaction.amount))}
              </p>
            </div>
          </div>
        ))}
        
        <button className="w-full text-center text-primary-600 hover:text-primary-700 font-medium text-sm py-2">
          View All Transactions
        </button>
      </div>
    </Card>
  )
}
