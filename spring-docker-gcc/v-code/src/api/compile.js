import { post } from '../utils/request'

export const compileApi = {
  compile: (code) => post('/compile', { code }),
  
  run: (taskId, args) => post('/run', { taskId: taskId, args }),
  
  compileAndRun: (code, args) => post('/compile_run', { code, args })
}

export default compileApi
