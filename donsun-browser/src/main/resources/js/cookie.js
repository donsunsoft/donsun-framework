var cookie = {
        get: function(name) {
            var c = document.cookie.match(new RegExp('(^|;)\\s*' + name + '=([^;\\s]*)'))
            return ((c && c.length >= 3) ? decodeURIComponent(c[2]) : null)
        },
        set: function(name, value, options) {
            options = options || {}
            var days = options.days, path = options.path, domain = options.domain, secure = options.secure
            if (days) {
                var d = new Date();
                d.setTime(d.getTime() + (days * 8.64e7)); 
            }
            document.cookie = name + '=' + encodeURIComponent(value) + (days ? ('; expires=' + d.toGMTString()) : '') + '; path=' + (path || '/') + (domain ? ('; domain=' + domain) : '') + (secure ? '; secure' : '')
        },
        remove: function(name, path, domain) {
            this.set(name, '', {days:-1, path:path, domain:domain})
        }
    };