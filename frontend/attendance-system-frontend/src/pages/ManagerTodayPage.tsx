import { useEffect, useState } from 'react'
import { managerService } from '../features/manager/managerService'
import type { TeamSummaryResponse, TodayStatusResponse } from '../types'
import StatCard from '../components/ui/StatCard'
import LoadingSpinner from '../components/common/LoadingSpinner'
import ErrorMessage from '../components/common/ErrorMessage'
import { getAxiosErrorMessage } from '../utils/formatters'

export default function ManagerTodayPage() {
  const [summary, setSummary] = useState<TeamSummaryResponse | null>(null)
  const [status, setStatus] = useState<TodayStatusResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [activeTab, setActiveTab] = useState<'present' | 'absent' | 'late'>('present')

  useEffect(() => {
    let cancelled = false
    const fetchData = async () => {
      setLoading(true)
      setError(null)
      try {
        const [sum, stat] = await Promise.all([
          managerService.getTeamSummary(),
          managerService.getTodayStatus(),
        ])
        if (!cancelled) {
          setSummary(sum)
          setStatus(stat)
        }
      } catch (err) {
        if (!cancelled) setError(getAxiosErrorMessage(err))
      } finally {
        if (!cancelled) setLoading(false)
      }
    }
    fetchData()
    return () => { cancelled = true }
  }, [])

  if (loading) return <LoadingSpinner size="lg" className="mt-20" />
  if (error) return <ErrorMessage message={error} onRetry={() => window.location.reload()} />

  const tabEmployees = {
    present: status?.presentEmployees ?? [],
    absent: status?.absentEmployees ?? [],
    late: status?.lateEmployees ?? [],
  }

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-gray-900">Today&apos;s Status</h1>

      {summary && (
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          <StatCard title="Total" value={summary.totalEmployees} icon="👥" colorClass="bg-blue-50 text-blue-600" />
          <StatCard title="Present" value={summary.presentToday} icon="✅" colorClass="bg-green-50 text-green-600" />
          <StatCard title="Absent" value={summary.absentToday} icon="❌" colorClass="bg-red-50 text-red-600" />
          <StatCard title="Late" value={summary.lateToday} icon="⏰" colorClass="bg-yellow-50 text-yellow-600" />
        </div>
      )}

      {status && (
        <div className="bg-white rounded-xl border border-gray-100 shadow-sm p-6">
          <div className="flex gap-1 mb-6 border-b border-gray-100">
            {(['present', 'absent', 'late'] as const).map((tab) => (
              <button
                key={tab}
                onClick={() => setActiveTab(tab)}
                className={`px-4 py-2 text-sm font-medium border-b-2 -mb-px transition-colors ${
                  activeTab === tab
                    ? 'border-blue-600 text-blue-600'
                    : 'border-transparent text-gray-500 hover:text-gray-700'
                }`}
              >
                {tab.charAt(0).toUpperCase() + tab.slice(1)} ({tabEmployees[tab].length})
              </button>
            ))}
          </div>

          <div className="space-y-3">
            {tabEmployees[activeTab].length === 0 ? (
              <p className="text-sm text-gray-400 py-4 text-center">No employees in this category.</p>
            ) : (
              tabEmployees[activeTab].map((emp) => (
                <div key={emp.id} className="flex items-center justify-between py-2 border-b border-gray-50 last:border-0">
                  <div>
                    <p className="text-sm font-medium text-gray-800">{emp.name}</p>
                    <p className="text-xs text-gray-500">{emp.employeeId} · {emp.department}</p>
                  </div>
                  {emp.checkInTime && (
                    <span className="text-xs text-gray-400">In: {emp.checkInTime}</span>
                  )}
                </div>
              ))
            )}
          </div>
        </div>
      )}
    </div>
  )
}
