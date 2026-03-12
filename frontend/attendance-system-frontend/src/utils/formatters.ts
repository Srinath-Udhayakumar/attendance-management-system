export function formatDate(dateString: string): string {
  return new Date(dateString).toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
  })
}

export function formatTime(dateTimeString: string | null): string {
  if (!dateTimeString) return '—'
  return new Date(dateTimeString).toLocaleTimeString('en-US', {
    hour: '2-digit',
    minute: '2-digit',
  })
}

export function formatHours(hours: number): string {
  const h = Math.floor(hours)
  const m = Math.round((hours - h) * 60)
  return `${h}h ${m}m`
}

export function statusColor(status: string): string {
  switch (status) {
    case 'PRESENT':
      return 'text-green-600 bg-green-50'
    case 'LATE':
      return 'text-yellow-600 bg-yellow-50'
    case 'ABSENT':
      return 'text-red-600 bg-red-50'
    case 'HALF_DAY':
      return 'text-blue-600 bg-blue-50'
    default:
      return 'text-gray-600 bg-gray-50'
  }
}

export function getAxiosErrorMessage(error: unknown): string {
  if (error && typeof error === 'object' && 'response' in error) {
    const axiosError = error as { response?: { data?: { message?: string } } }
    return axiosError.response?.data?.message ?? 'An unexpected error occurred'
  }
  if (error instanceof Error) return error.message
  return 'An unexpected error occurred'
}
