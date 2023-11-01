import request from '@/request'

export function myCollect(userId) {
    return request({
        url: `/collect/list/${userId}`,
        method: 'post'
    })
}

export function addCollect(collect,token) {
    return request({
        headers: {'Authorization': token},
        url: '/collect/add',
        method: 'post',
        data: collect
    })
}

export function deleteCollect(collect,token) {
    return request({
        headers: {'Authorization': token},
        url: '/collect/delete',
        method: 'post',
        data: collect
    })
}

export function collectCount(articleId) {
    return request({
        url: `/collect/count/${articleId}`,
        method: 'post'
    })
}