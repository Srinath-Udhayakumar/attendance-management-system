import { useEffect, useState } from 'react'
import { managerService } from '../features/manager/managerService'
import type { ManagerDashboardResponse } from '../types'
import StatCard from '../components/ui/StatCard'
import LoadingSpinner from '../components/common/LoadingSpinner'
import ErrorMessage from '../components/common/ErrorMessage'
import { getAxiosErrorMessage } from '../utils/formatters'

export default function ManagerDashboardPage() {
  const [dashboard, setDashboard] = useState<ManagerDashboardResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    let cancelled = false
    const fetchData = async () => {
      setLoading(true)
      setError(null)
      try {
        const data = await managerService.getDashboard()
        if (!cancelled) setDashboard(data)
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
  if (!dashboard) return null

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-gray-900">Manager Overview</h1>

      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <StatCard title="Total Employees" value={dashboard.totalEmployees} icon="👥" colorClass="bg-blue-50 text-blue-600" />
        <StatCard title="Present Today" value={dashboard.presentToday} icon="✅" colorClass="bg-green-50 text-green-600" />
        <StatCard title="Absent Today" value={dashboard.absentToday} icon="❌" colorClass="bg-red-50 text-red-600" />
        <StatCard title="Late Today" value={dashboard.lateToday} icon="⏰" colorClass="bg-yellow-50 text-yellow-600" />
      </div>

      {dashboard.departmentStats.length > 0 && (
        <div className="bg-white rounded-xl border border-gray-100 shadow-sm p-6">
          <h2 className="text-base font-semibold text-gray-900 mb-4">Department Stats</h2>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="text-left text-gray-500 border-b border-gray-100">
                  <th className="pb-3 font-medium">Department</th>
                  <th className="pb-3 font-medium">Total</th>
                  <th className="pb-3 font-medium">Present</th>
                  <th className="pb-3 font-medium">Absent</th>
                  <th className="pb-3 font-medium">Late</th>
                  <th className="pb-3 font-medium">Avg Hours</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-50">
                {dashboard.departmentStats.map((dept) => (
                  <tr key={dept.departmentName}>
                    <td className="py-3 font-medium text-gray-800">{dept.departmentName}</td>
                    <td className="py-3 text-gray-600">{dept.totalEmployees}</td>
                    <td className="py-3 text-green-600">{dept.presentToday}</td>
                    <td className="py-3 text-red-600">{dept.absentToday}</td>
                    <td className="py-3 text-yellow-600">{dept.lateToday}</td>
                    <td className="py-3 text-gray-600">{dept.averageHoursWorked.toFixed(1)}h</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {dashboard.absentEmployeesToday.length > 0 && (
        <div className="bg-white rounded-xl border border-gray-100 shadow-sm p-6">
          <h2 className="text-base font-semibold text-gray-900 mb-4">Absent Today</h2>
          <div className="space-y-3">
            {dashboard.absentEmployeesToday.map((emp) => (
              <div key={emp.employeeId} className="flex items-center justify-between py-2 border-b border-gray-50 last:border-0">
                <div>
                  <p className="text-sm font-medium text-gray-800">{emp.name}</p>
                  <p className="text-xs text-gray-500">{emp.employeeId} · {emp.departmentName}</p>
                </div>
                <span className="text-xs text-gray-400">{emp.email}</span>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}
