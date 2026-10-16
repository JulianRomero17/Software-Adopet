import { Moon, Sun } from 'lucide-react'
import { useTheme } from '../context/ThemeContext'

export default function ThemeToggle() {
  const { theme, toggleTheme } = useTheme()
  const dark = theme === 'dark'

  return <button
    type="button"
    onClick={toggleTheme}
    aria-label={dark ? 'Cambiar a modo claro' : 'Cambiar a modo oscuro'}
    aria-pressed={dark}
    className="inline-flex items-center gap-2 rounded-xl border border-[#EEE7DD] bg-white px-3.5 py-2.5 text-sm font-semibold text-[#746C63] transition hover:bg-cream"
  >
    {dark ? <Sun size={17} /> : <Moon size={17} />}
    <span>{dark ? 'Modo claro' : 'Modo oscuro'}</span>
  </button>
}
