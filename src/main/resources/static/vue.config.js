const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  outputDir: 'hbq969-sm',
  publicPath: './',
  devServer: {
    proxy: {
      '/dev': {
        target: `http://localhost:30148`,
        changeOrigin: true,
        secure: false,
        pathRewrite: { '^/dev': '' },
      },
    },
  }
})
