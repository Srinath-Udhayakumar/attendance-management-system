import { useEffect, useState, useCallback } from 'react'
import { useAuthContext } from '../app/providers/AuthContext'
import { employeeService } from '../features/employee/employeeService'
import type { AttendanceResponse, EmployeeDashboardResponse } from '../types'
import StatCard from '../components/ui/StatCard'
import Badge from '../components/ui/Badge'
import LoadingSpinner from '../components/common/LoadingSpinner'
import ErrorMessage from '../components/common/ErrorMessage'
import { formatTime, formatHours, getAxiosErrorMessage } from '../utils/formatters'

export default function EmployeeDashboardPage() {
  const { user } = useAuthContext()
  const [dashboard, setDashboard] = useState<EmployeeDashboardResponse | null>(null)
  const [today, setToday] = useState<AttendanceResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [actionLoading, setActionLoading] = useState(false)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const [dash, todayAtt] = await Promise.all([
        employeeService.getDashboard(),
        employeeService.getTodayAttendance(),
      ])
      setDashboard(dash)
      setToday(todayAtt)
    } catch (err) {
      setError(getAxiosErrorMessage(err))
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => { fetchData() }, [fetchData])

  const handleCheckIn = async () => {
    setActionLoading(true)
    try {
      const att = await employeeService.checkIn()
      setToday(att)
    } catch (err) {
      setError(getAxiosErrorMessage(err))
    } finally {
      setActionLoading(false)
    }
  }

  const handleCheckOut = async () => {
    setActionLoading(true)
    try {
      const att = await employeeService.checkOut()
      setToday(att)
    } catch (err) {
      setError(getAxiosErrorMessage(err))
    } finally {
      setActionLoading(false)
    }
  }

  if (loading) return <LoadingSpinner size="lg" className="mt-20" />
  if (error) return <ErrorMessage message={error} onRetry={fetchData} />

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Welcome back, {user?.name} 👋</h1>
        <p className="text-sm text-gray-500">{new Date().toDateString()}</p>
      </div>

      <div className="bg-white rounded-xl border border-gray-100 shadow-sm p-6">
        <div className="flex flex-wrap items-center justify-between gap-4">
          <div>
            <p className="text-sm text-gray-500">Today&apos;s Status</p>
            {today ? (
              <div className="mt-1 space-y-1">
                <Badge status={today.status} />
                <p className="text-xs text-gray-400">
                  In: {formatTime(today.checkInTime)} · Out: {formatTime(today.checkOutTime)}
                </p>
              </div>
            ) : (
              <p className="text-sm text-gray-400 mt-1">Not checked in yet</p>
            )}
          </div>
          <div className="flex gap-3">
            {!today?.checkInTime && (
              <button
                onClick={handleCheckIn}
                disabled={actionLoading}
                className="rounded-lg bg-green-600 px-5 py-2 text-sm font-medium text-white hover:bg-green-700 disabled:opacity-50"
              >
                {actionLoading ? '…' : 'Check In'}
              </button>
            )}
            {today?.checkInTime && !today.checkOutTime && (
              <button
                onClick={handleCheckOut}
                disabled={actionLoading}
                className="rounded-lg bg-orange-500 px-5 py-2 text-sm font-medium text-white hover:bg-orange-600 disabled:opacity-50"
              >
                {actionLoading ? '…' : 'Check Out'}
              </button>
            )}
          </div>
        </div>
      </div>

      {dashboard && (
        <>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <StatCard title="Present" value={dashboard.presentCount} icon="✅" colorClass="bg-green-50 text-green-600" />
            <StatCard title="Absent" value={dashboard.absentCount} icon="❌" colorClass="bg-red-50 text-red-600" />
            <StatCard title="Late" value={dashboard.lateCount} icon="⏰" colorClass="bg-yellow-50 text-yellow-600" />
            <StatCard title="Hours This Month" value={formatHours(dashboard.totalHoursThisMonth)} icon="⏱" colorClass="bg-blue-50 text-blue-600" />
          </div>

          {dashboard.last7DaysAttendance.length > 0 && (
            <div className="bg-white rounded-xl border border-gray-100 shadow-sm p-6">
              <h2 className="text-base font-semibold text-gray-900 mb-4">Last 7 Days</h2>
              <div className="overflow-x-auto">
                <table className="w-full text-sm">
                  <thead>
                    <tr className="text-left text-gray-500 border-b border-gray-100">
                      <th className="pb-3 font-medium">Date</th>
                      <th className="pb-3 font-medium">Status</th>
                      <th className="pb-3 font-medium">Hours</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-gray-50">
                    {dashboard.last7DaysAttendance.map((d) => (
                      <tr key={d.date}>
                        <td className="py-2 text-gray-700">{new Date(d.date).toLocaleDateString('en-US', { weekday: 'short', month: 'short', day: 'numeric' })}</td>
                        <td className="py-2"><Badge status={d.status} /></td>
                        <td className="py-2 text-gray-600">{formatHours(d.hoursWorked)}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}
        </>
      )}
    </div>
  )
}
