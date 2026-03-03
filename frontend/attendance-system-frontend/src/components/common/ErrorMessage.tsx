interface Props {
  message: string
  onRetry?: () => void
}

export default function ErrorMessage({ message, onRetry }: Props) {
  return (
    <div className="rounded-md bg-red-50 p-4">
      <div className="flex items-start gap-3">
        <span className="text-red-400 text-xl">⚠</span>
        <div className="flex-1">
          <p className="text-sm text-red-700">{message}</p>
          {onRetry && (
            <button
              onClick={onRetry}
              className="mt-2 text-sm font-medium text-red-600 hover:text-red-500"
            >
              Try again
            </button>
          )}
        </div>
      </div>
    </div>
  )
}
