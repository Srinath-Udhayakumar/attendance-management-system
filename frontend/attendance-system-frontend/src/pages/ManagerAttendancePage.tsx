import { useEffect, useState } from 'react'
import { managerService } from '../features/manager/managerService'
import type { AttendanceResponse, PageResponse } from '../types'
import Badge from '../components/ui/Badge'
import LoadingSpinner from '../components/common/LoadingSpinner'
import ErrorMessage from '../components/common/ErrorMessage'
import EmptyState from '../components/common/EmptyState'
import { formatDate, formatTime, formatHours, getAxiosErrorMessage } from '../utils/formatters'

export default function ManagerAttendancePage() {
  const todayStr = new Date().toISOString().split('T')[0]
  const [date, setDate] = useState(todayStr)
  const [page, setPage] = useState(0)
  const [data, setData] = useState<PageResponse<AttendanceResponse> | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [approvingId, setApprovingId] = useState<string | null>(null)
  const [exportLoading, setExportLoading] = useState(false)

  useEffect(() => {
    let cancelled = false
    const fetchData = async () => {
      setLoading(true)
      setError(null)
      try {
        const result = await managerService.getAllAttendances(date, page)
        if (!cancelled) setData(result)
      } catch (err) {
        if (!cancelled) setError(getAxiosErrorMessage(err))
      } finally {
        if (!cancelled) setLoading(false)
      }
    }
    fetchData()
    return () => { cancelled = true }
  }, [date, page])

  const handleApproveLate = async (attendanceId: string) => {
    setApprovingId(attendanceId)
    try {
      const updated = await managerService.approveLate(attendanceId)
      setData((prev) =>
        prev
          ? {
              ...prev,
              content: prev.content.map((a) => (a.id === attendanceId ? updated : a)),
            }
          : prev,
      )
    } catch (err) {
      setError(getAxiosErrorMessage(err))
    } finally {
      setApprovingId(null)
    }
  }

  const handleExport = async () => {
    setExportLoading(true)
    try {
      await managerService.exportCSV(date, date)
    } catch (err) {
      setError(getAxiosErrorMessage(err))
    } finally {
      setExportLoading(false)
    }
  }

  if (error) return <ErrorMessage message={error} onRetry={() => setPage((p) => p)} />

  return (
    <div className="space-y-6">
      <div className="flex flex-wrap items-center justify-between gap-4">
        <h1 className="text-2xl font-bold text-gray-900">Team Attendance</h1>
        <button
          onClick={handleExport}
          disabled={exportLoading}
          className="rounded-lg border border-gray-300 px-4 py-2 text-sm hover:bg-gray-50 disabled:opacity-50"
        >
          {exportLoading ? 'Exporting…' : '⬇ Export CSV'}
        </button>
      </div>

      <div className="bg-white rounded-xl border border-gray-100 shadow-sm p-6">
        <div className="mb-6">
          <label className="block text-xs font-medium text-gray-500 mb-1">Date</label>
          <input
            type="date"
            value={date}
            onChange={(e) => { setDate(e.target.value); setPage(0) }}
            className="rounded-lg border border-gray-300 px-3 py-1.5 text-sm"
          />
        </div>

        {loading ? (
          <LoadingSpinner size="md" className="py-8" />
        ) : !data || data.content.length === 0 ? (
          <EmptyState title="No attendance records" description={`No attendance found for ${formatDate(date)}.`} />
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
                    <th className="pb-3 font-medium">Action</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-50">
                  {data.content.map((att) => (
                    <tr key={att.id}>
                      <td className="py-3 text-gray-700">{formatDate(att.date)}</td>
                      <td className="py-3"><Badge status={att.status} /></td>
                      <td className="py-3 text-gray-600">{formatTime(att.checkInTime)}</td>
                      <td className="py-3 text-gray-600">{formatTime(att.checkOutTime)}</td>
                      <td className="py-3 text-gray-600">{formatHours(att.totalHours)}</td>
                      <td className="py-3">
                        {att.status === 'LATE' && !att.lateApproved && (
                          <button
                            onClick={() => handleApproveLate(att.id)}
                            disabled={approvingId === att.id}
                            className="text-xs rounded bg-blue-50 text-blue-600 px-2 py-1 hover:bg-blue-100 disabled:opacity-50"
                          >
                            {approvingId === att.id ? '…' : 'Approve Late'}
                          </button>
                        )}
                        {att.lateApproved && (
                          <span className="text-xs text-green-600">✓ Approved</span>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
            <div className="flex items-center justify-between mt-4">
              <p className="text-xs text-gray-500">
                Page {data.number + 1} of {data.totalPages}
              </p>
              <div className="flex gap-2">
                <button
                  disabled={data.first}
                  onClick={() => setPage((p) => p - 1)}
                  className="rounded border border-gray-300 px-3 py-1 text-xs disabled:opacity-40 hover:bg-gray-50"
                >
                  Previous
                </button>
                <button
                  disabled={data.last}
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
