import React from 'react'
import { cn } from '../../lib/utils'

export default function Input({
  label,
  error,
  helperText,
  className = '',
  labelClassName = '',
  ...props
}) {
  return (
    <div className="space-y-1">
      {label && (
        <label className={cn("block text-sm font-medium text-gray-700", labelClassName)}>
          {label}
        </label>
      )}
      <input
        className={cn(
          'input',
          error && 'border-error-500 focus:border-error-500 focus:ring-error-500',
          className
        )}
        {...props}
      />
      {error && (
        <p className="text-sm text-error-600">{error}</p>
      )}
      {helperText && !error && (
        <p className="text-sm text-gray-500">{helperText}</p>
      )}
    </div>
  )
}
