import { useEffect, useState } from 'react'
import { employeeService } from '../features/employee/employeeService'
import type { AttendanceResponse, MonthlyAttendanceSummaryDTO, PageResponse } from '../types'
import Badge from '../components/ui/Badge'
import StatCard from '../components/ui/StatCard'
import LoadingSpinner from '../components/common/LoadingSpinner'
import ErrorMessage from '../components/common/ErrorMessage'
import EmptyState from '../components/common/EmptyState'
import { formatDate, formatTime, formatHours, getAxiosErrorMessage } from '../utils/formatters'

export default function AttendancePage() {
  const [currentMonth] = useState(() => new Date().getMonth() + 1)
  const [currentYear] = useState(() => new Date().getFullYear())
  const now = new Date()
  const [from, setFrom] = useState(
    new Date(now.getFullYear(), now.getMonth(), 1).toISOString().split('T')[0],
  )
  const [to, setTo] = useState(now.toISOString().split('T')[0])
  const [page, setPage] = useState(0)
  const [history, setHistory] = useState<PageResponse<AttendanceResponse> | null>(null)
  const [monthly, setMonthly] = useState<MonthlyAttendanceSummaryDTO | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    let cancelled = false
    const fetchData = async () => {
      setLoading(true)
      setError(null)
      try {
        const [hist, mon] = await Promise.all([
          employeeService.getAttendanceHistory(from, to, page),
          employeeService.getMonthlyAttendance(currentMonth, currentYear),
        ])
        if (!cancelled) {
          setHistory(hist)
          setMonthly(mon)
        }
      } catch (err) {
        if (!cancelled) setError(getAxiosErrorMessage(err))
      } finally {
        if (!cancelled) setLoading(false)
      }
    }
    fetchData()
    return () => { cancelled = true }
  }, [from, to, page, currentMonth, currentYear])

  if (error) return <ErrorMessage message={error} onRetry={() => setPage((p) => p)} />

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-gray-900">My Attendance</h1>

      {monthly && (
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          <StatCard title="Present" value={monthly.presentDays} icon="✅" colorClass="bg-green-50 text-green-600" />
          <StatCard title="Absent" value={monthly.absentDays} icon="❌" colorClass="bg-red-50 text-red-600" />
          <StatCard title="Late" value={monthly.lateDays} icon="⏰" colorClass="bg-yellow-50 text-yellow-600" />
          <StatCard title="Attendance %" value={`${monthly.attendancePercentage.toFixed(1)}%`} icon="📊" colorClass="bg-blue-50 text-blue-600" />
        </div>
      )}

      <div className="bg-white rounded-xl border border-gray-100 shadow-sm p-6">
        <div className="flex flex-wrap gap-4 mb-6">
          <div>
            <label className="block text-xs font-medium text-gray-500 mb-1">From</label>
            <input
              type="date"
              value={from}
              onChange={(e) => { setFrom(e.target.value); setPage(0) }}
              className="rounded-lg border border-gray-300 px-3 py-1.5 text-sm"
            />
          </div>
          <div>
            <label className="block text-xs font-medium text-gray-500 mb-1">To</label>
            <input
              type="date"
              value={to}
              onChange={(e) => { setTo(e.target.value); setPage(0) }}
              className="rounded-lg border border-gray-300 px-3 py-1.5 text-sm"
            />
          </div>
        </div>

        {loading ? (
          <LoadingSpinner size="md" className="py-8" />
        ) : !history || history.content.length === 0 ? (
          <EmptyState title="No attendance records" description="No records found for the selected period." />
        ) : (
          <>
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="text-left text-gray-500 border-b border-gray-100">
                    <th className="pb-3 font-medium">Date</th>
                    <th className="pb-3 font-medium">Status</th>
                    <th className="pb-3 font-medium">Check In</th>
                    <th className="pb-3 font-medium">Check Out</th>
                    <th className="pb-3 font-medium">Hours</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-50">
                  {history.content.map((att) => (
                    <tr key={att.id}>
                      <td className="py-3 text-gray-700">{formatDate(att.date)}</td>
                      <td className="py-3"><Badge status={att.status} /></td>
                      <td className="py-3 text-gray-600">{formatTime(att.checkInTime)}</td>
                      <td className="py-3 text-gray-600">{formatTime(att.checkOutTime)}</td>
                      <td className="py-3 text-gray-600">{formatHours(att.totalHours)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
            <div className="flex items-center justify-between mt-4">
              <p className="text-xs text-gray-500">
                Showing {history.number * history.size + 1}–{Math.min((history.number + 1) * history.size, history.totalElements)} of {history.totalElements}
              </p>
              <div className="flex gap-2">
                <button
                  disabled={history.first}
                  onClick={() => setPage((p) => p - 1)}
                  className="rounded border border-gray-300 px-3 py-1 text-xs disabled:opacity-40 hover:bg-gray-50"
                >
                  Previous
                </button>
                <button
                  disabled={history.last}
                  onClick={() => setPage((p) => p + 1)}
                  className="rounded border border-gray-300 px-3 py-1 text-xs disabled:opacity-40 hover:bg-gray-50"
                >
                  Next
                </button>
              </div>
            </div>
          </>
        )}
      </div>
    </div>
  )
}
