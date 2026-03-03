interface Props {
  title: string
  value: string | number
  icon: string
  colorClass?: string
}

export default function StatCard({ title, value, icon, colorClass = 'bg-blue-50 text-blue-600' }: Props) {
  return (
    <div className="rounded-xl bg-white p-6 shadow-sm border border-gray-100">
      <div className="flex items-center gap-4">
        <div className={`rounded-lg p-3 ${colorClass}`}>
          <span className="text-2xl">{icon}</span>
        </div>
        <div>
          <p className="text-sm text-gray-500">{title}</p>
          <p className="text-2xl font-bold text-gray-900">{value}</p>
        </div>
      </div>
    </div>
  )
}
