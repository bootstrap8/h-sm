import {ElNotification, ElMessage} from 'element-plus'
import type {EpPropMergeType} from "element-plus/es/utils/vue/props/types"
import CryptoJS from "crypto-js"
import axios from '@/network/index'

export function jsonPretty(str: string, tab?: number | undefined): string {
    const obj = JSON.parse(str)
    return tab == undefined ?
        JSON.stringify(obj, null, 2) :
        JSON.stringify(obj, null, tab);
}

export function notify(title: string, message: string,
                       type: EpPropMergeType<StringConstructor,
                           "success" | "warning" | "info" | "error", unknown>): void {
    ElNotification({
        title: '操作',
        message: '操作成功',
        type: type,
        position: "bottom-right"
    })
}

export function msg(message: string,
                    type: EpPropMergeType<StringConstructor,
                        "success" | "warning" | "info" | "error", unknown>): void {
    ElMessage({
        message: message,
        type: type,
    })
}

export function objectEmpty(obj: any): boolean {
    return obj && Object.keys(obj).length == 0
}

export class Page {
    pageNum: number;
    pageSize: number;
    total: number;

    constructor(pageNum: number, pageSize: number, total: number) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
    }

}

export function downFile(options: any) {
    axios(options).then((res: any) => {
        const blob = new Blob([res.data]);
        const fileName = options.folderName + '.' + options.fileExtension;
        if ('download' in document.createElement('a')) { // 非IE下载
            const elink = document.createElement('a');
            elink.download = fileName;
            elink.style.display = 'none';
            elink.href = URL.createObjectURL(blob);
            document.body.appendChild(elink);
            elink.click();
            URL.revokeObjectURL(elink.href);// 释放URL 对象
            document.body.removeChild(elink);
        } else { // IE10+下载
            // if (navigator) {
            //   navigator.msSaveBlob(blob, fileName);
            // }
        }
        msg('导出成功', 'success')
    }).catch((err: any) => {
        console.error(err)
        msg('导出异常', 'error')
    });
}

// 混淆函数
export function obfuscate(input: any, key: any): string {
    let result = '';
    for (let i = 0; i < input.length; i++) {
        result += String.fromCharCode(input.charCodeAt(i) ^ key);
    }
    return result;
}

// 还原函数
export function deobfuscate(input: any, key: any): string {
    return obfuscate(input, key);
}

export function decryptAES(word: any, key: any, iv: any): string {
    let base64 = CryptoJS.enc.Base64.parse(word);
    let src = CryptoJS.enc.Base64.stringify(base64);
    // 解密模式为CBC，补码方式为PKCS5Padding（也就是PKCS7）
    let decrypt = CryptoJS.AES.decrypt(src, key, {
        iv: iv,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
    });
    var decryptedStr = decrypt.toString(CryptoJS.enc.Utf8);
    return decryptedStr.toString();
}

//加密方法
export function encryptAES(word: any, key: any, iv: any): string {
    let srcs = CryptoJS.enc.Utf8.parse(word);
    // 加密模式为CBC，补码方式为PKCS5Padding（也就是PKCS7）
    let encrypted = CryptoJS.AES.encrypt(srcs, key, {
        iv: iv,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
    });
    //返回base64
    return CryptoJS.enc.Base64.stringify(encrypted.ciphertext);
}