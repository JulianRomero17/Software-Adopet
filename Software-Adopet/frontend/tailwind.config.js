/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,jsx}'],
  theme: {
    extend: {
      colors: {
        ink: '#34333A',
        coral: {
          50: '#FFF5F0',
          100: '#FFE6DA',
          300: '#FFB39A',
          500: '#EE7659',
          600: '#D95F44',
          700: '#B84832'
        },
        sage: {
          50: '#F1F6EF',
          100: '#DFEBDD',
          300: '#AFC8AB',
          500: '#759B74',
          700: '#4E7351'
        },
        cream: '#FBF8F2',
        sunshine: '#F4C867'
      },
      fontFamily: {
        display: ['Quicksand', 'sans-serif'],
        sans: ['Inter', 'sans-serif']
      },
      boxShadow: {
        soft: '0 16px 40px rgba(79, 60, 48, 0.08)',
        float: '0 24px 50px rgba(79, 60, 48, 0.14)'
      }
    }
  },
  plugins: []
}
