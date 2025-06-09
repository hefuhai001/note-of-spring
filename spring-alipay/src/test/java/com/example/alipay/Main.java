package com.example.alipay;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.factory.Factory.Payment;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;

public class Main {
    public static void main(String[] args) throws Exception {
        // 1. 设置参数（全局只需设置一次）
        Factory.setOptions(getOptions());
        try {
            // 2. 发起API调用（以创建当面付收款二维码为例）
            AlipayTradePrecreateResponse response = Payment.FaceToFace()
                    .preCreate("Apple iPhone11 128G", "2234567890", "5799.00");
            // 3. 处理响应或异常
            if (ResponseChecker.success(response)) {
                System.out.println("调用成功");
            } else {
                System.err.println("调用失败，原因：" + response.msg + "，" + response.subMsg);
            }
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static Config getOptions() {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipay.com";
        config.signType = "RSA2";
        config.appId = "9021000132681525";
        // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
        config.merchantPrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCKm/jC5Gh6GDMk6YKckzh53wB24Vi+ojbkv+R+ulotMxLSCX5i++qN1MpW1crGW+tLFaDkW/9U+xO80YM1hx+tOmox/oEz0ivUSYI+cZXBfbaLmM5Pqkq2TCqJYzCfh3E/JyIGCVWbMqE0+qp/mK8PCofYtyZCf4hvanKA70SlZmHGukTy31MpkIso/cidDC0pSrSIvsje4wT1BYNBz56zulyc/EiYWoG5TsqQ4dxnJKXmeEPpBu2dOMiJygQPDSIpoYoFSP6+2mLbb4dSyQyAAQud1K+loVKavUv53wbUZurCwVVYtxquGYsYPKPU+2FnDECqgtaPyw8MiV124ZF7AgMBAAECggEAALVhZSWjvxQGlua2qzQ5cq4II5NtT7Qmka7FbTxon5iGPRqdUcY14bm3uw/mtHLd1glrRB+QQ4/w/uMXR5vENq94wX6qjGhDnxctGkKcYFvTJg2G6HNXZG0pPoCKTBZ2Jrt9eghsLpnopwus+W//XRVRsnLlOCvMt70UjsIqWKoWYMBoA24jOqwrzHiN/nuHjILNswHaA14im6xxt3bOyF5dBpud3fsCNJCM4tGsbbsB6H++VRnrpxd14aCwUM12F3KK0M8GokhkVtPPbX4rTgbQ1YT8XYz8mRM41wqL+q1D+FfOa2rsowE/VT9iUHRAmlVQJS7m2e5SnojnLs8jyQKBgQDOmx7XgmlA2nIULBcxMkrKss4GoHmEscdB96a2+o1MxVFuZqx2nzT+216i/owUe92vHqcV9pP1ql/MYjgIs3D6j60NfinXOwwsMbTmhE1sdKdYj1rFISrf1li/jV5zZklDdy8jRDsAwBDzidRw2JyKs1SPKoBLyD+g7DfiHgdK3wKBgQCrv0IyuPFdV/u3c9e3e+5yes2FjOmMmwlwmsBQjSmtf34GsBbocTe1oP2Qvv55qf0dXdzFX4iNt+tYjYja4wpHAKKEqmUe33y8Ogtvqt/jJHnmw7luT3cZT5xmA4Od9TBDfKY+5l8FWd8RcllAGb2NqFGxHuxaw5c8kEDiuy9o5QKBgQCSs7SvpAsmoLXWOwRwy1wuF86RCAuhPDbXUYkQblI8y/MpaEBXrm6L9Kp7JDHFLbrYaPSNdNvU2ZhSYP+Wtgun7rMbXJVLHcNj8huYfyMWurJFAO2cAhQWgnXb9f62BFBLVmqjKQSYYMjolNrDEhv9pJ6LVctphBQiKPj5VwcokwKBgChIuDJFhveqB3TESzjSbN+VRleo8a71PoWXBrzeH4R+fwT6kKT8yWBk3pTw96d549Dbj0kMW29CUIV4zTzqPCLjVzte7FENgqhzxPLwVYtlpuIlzny9TuY08i5LbLfEJwntVuEyD9+lcNkaxSxv3TI45NByvGWdsv+VpmKIHVqxAoGBAITqrHVzSqczbwoqYmPFx63GT5TZkT8Z7CZYOivXQ7GjFvY+m72+oxNhXQdOy5uy8ucQ+EPC2ZQINDzXhRMsc8UEA30VreVmN+LI0FqFHPkSszMJ0z1UsCyunB2vcwl4MJTqEHQ5hlcozQcSzODJ9IFyu7Ia0YvqDJuOJaYqJo4b";
        //注：证书文件路径支持设置为文件系统中的路径或CLASS_PATH中的路径，优先从文件系统中加载，加载失败后会继续尝试从CLASS_PATH中加载
        config.merchantCertPath = "<-- 请填写您的应用公钥证书文件路径，例如：/foo/appCertPublicKey_2019051064521003.crt -->";
        config.alipayCertPath = "<-- 请填写您的支付宝公钥证书文件路径，例如：/foo/alipayCertPublicKey_RSA2.crt -->";
        config.alipayRootCertPath = "<-- 请填写您的支付宝根证书文件路径，例如：/foo/alipayRootCert.crt -->";
        //注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
        config.alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmtltgUOyQ3USabrwF6yKSKYyS3F4Q7e2rqeLxAFZ44PiJPlYFx6lToM3dESR1GXbS1uMdgDtr7QR2rAH2spyhZJLDjXsaYbjfzPUCLESgOM/a+Ob/8Q7FpKPsf4tFBV8m4sZNpDKZxiSF19EcXtQ91m9EmxZCBKA7idhfo/s82XCyXh9wBrE7kL59X203Xi7JPx960LfTu4Xj62Bh9NksBIJdr3WTkcF59pDA3pcKit/gF73MQ6LNW0hdf3+Uovfc1FqE8sH/75xkWleDNvpJbvhishrwfC+mYJcBpNdulUo4tRtEtG8KoYMc3IPHe/hC+rAAQtWToqn8O2t9lkgIQIDAQAB";
        //可设置异步通知接收服务地址（可选）
        config.notifyUrl = "<-- 请填写您的支付类接口异步通知接收服务地址，例如：https://www.test.com/callback -->";
        //可设置AES密钥，调用AES加解密相关接口时需要（可选）
        config.encryptKey = "kHDBrUuOijItu2D+EHqEgw==";
        return config;
    }
}
