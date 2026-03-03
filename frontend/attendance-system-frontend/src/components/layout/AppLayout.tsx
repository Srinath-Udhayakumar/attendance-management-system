import { Outlet } from 'react-router-dom'
import type { AuthMeResponse } from '../../types'
import Navbar from './Navbar'
import Sidebar from './Sidebar'

interface Props {
  user: AuthMeResponse | null
  onLogout: () => void
}

export default function AppLayout({ user, onLogout }: Props) {
  return (
    <div className="flex flex-col min-h-screen bg-gray-50">
      <Navbar user={user} onLogout={onLogout} />
      <div className="flex flex-1">
        {user && <Sidebar role={user.role} />}
        <main className="flex-1 p-6 overflow-auto">
          <Outlet />
        </main>
      </div>
    </div>
  )
}
