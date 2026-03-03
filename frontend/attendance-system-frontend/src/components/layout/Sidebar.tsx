import { NavLink } from 'react-router-dom'

interface NavItem {
  to: string
  icon: string
  label: string
}

interface Props {
  role: string
}

export default function Sidebar({ role }: Props) {
  const employeeLinks: NavItem[] = [
    { to: '/dashboard', icon: '🏠', label: 'Dashboard' },
    { to: '/attendance', icon: '📋', label: 'My Attendance' },
  ]

  const managerLinks: NavItem[] = [
    { to: '/manager/dashboard', icon: '📊', label: 'Overview' },
    { to: '/manager/attendance', icon: '👥', label: 'Team Attendance' },
    { to: '/manager/today', icon: '📅', label: "Today's Status" },
  ]

  const links = role === 'MANAGER' || role === 'ADMIN' ? managerLinks : employeeLinks

  return (
    <aside className="w-60 bg-gray-900 min-h-screen hidden md:block">
      <nav className="p-4 space-y-1">
        {links.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            className={({ isActive }) =>
              `flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm transition-colors ${
                isActive
                  ? 'bg-blue-600 text-white'
                  : 'text-gray-400 hover:bg-gray-800 hover:text-white'
              }`
            }
          >
            <span>{item.icon}</span>
            <span>{item.label}</span>
          </NavLink>
        ))}
      </nav>
    </aside>
  )
}
