import { statusColor } from '../../utils/formatters'

interface Props {
  status: string
  label?: string
}

export default function Badge({ status, label }: Props) {
  return (
    <span className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium ${statusColor(status)}`}>
      {label ?? status}
    </span>
  )
}
