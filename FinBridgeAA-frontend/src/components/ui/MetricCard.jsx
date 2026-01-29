import React from 'react'
import { cn, formatCurrency, calculatePercentageChange, getColorForValue } from '../../lib/utils'
import { TrendingUp, TrendingDown, Minus } from 'lucide-react'

export default function MetricCard({
  title,
  value,
  change,
  changeType = 'percentage',
  icon: Icon,
  format = 'currency',
  className = '',
}) {
  const isPositive = change > 0
  const isNegative = change < 0
  const isNeutral = change === 0

  const formatValue = (val) => {
    if (format === 'currency') return formatCurrency(val)
    if (format === 'number') return val.toLocaleString('en-IN')
    return val
  }

  const formatChange = (val) => {
    if (changeType === 'percentage') {
      return `${isPositive ? '+' : ''}${val.toFixed(1)}%`
    }
    if (format === 'currency') {
      return `${isPositive ? '+' : ''}${formatCurrency(Math.abs(val))}`
    }
    return `${isPositive ? '+' : ''}${val}`
  }

  const TrendIcon = isPositive ? TrendingUp : isNegative ? TrendingDown : Minus

  return (
    <div className={cn('metric-card', className)}>
      <div className="flex items-center justify-between">
        <div className="flex-1">
          <p className="metric-label">{title}</p>
          <p className="metric-value">{formatValue(value)}</p>
          {change !== undefined && (
            <div className="flex items-center mt-2">
              <TrendIcon
                className={cn(
                  'w-4 h-4 mr-1',
                  getColorForValue(change, 'change')
                )}
              />
              <span className={cn('metric-change', getColorForValue(change, 'change'))}>
                {formatChange(change)}
              </span>
            </div>
          )}
        </div>
        {Icon && (
          <div className="p-3 bg-gray-50 rounded-lg">
            <Icon className="w-6 h-6 text-gray-600" />
          </div>
        )}
      </div>
    </div>
  )
}
