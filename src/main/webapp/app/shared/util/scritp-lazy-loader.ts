export class ScriptLazyLoader {
    public static loadScript(url:string) {
        console.log('preparing to load...')
        let node = document.createElement('script');
        node.src = url;
        node.type = 'text/javascript';
        node.async = true;
        node.charset = 'utf-8';
        document.getElementsByTagName('head')[0].appendChild(node);
    }

    public static loadCss(url:string) {
        console.log('preparing to load...')
        let node = document.createElement('link');
        node.rel = "stylesheet";
        node.type = "text/css"
        node.href = url;
        node.charset = 'utf-8';
        document.getElementsByTagName('head')[0].appendChild(node);
    }
}
