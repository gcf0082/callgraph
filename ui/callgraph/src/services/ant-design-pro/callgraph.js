import { request } from 'umi';


export async function getCallerGraph(params, options) {
    return request('/caller_graph', {
      method: 'GET',
      params: { ...params },
      ...(options || {}),
    });
  }