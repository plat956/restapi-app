if (!library)
    var library = {};

library.json = {
    replacer: function(match, pIndent, pKey, pVal, pEnd) {
        var key = '<span class=json-key>';
        var val = '<span class=json-value>';
        var str = '<span class=json-string>';
        var r = pIndent || '';
        if (pKey)
            r = r + key + pKey.replace(/[": ]/g, '') + '</span>: ';
        if (pVal)
            r = r + (pVal[0] == '"' ? str : val) + pVal + '</span>';
        return r + (pEnd || '');
    },
    prettyPrint: function(obj) {
        var jsonLine = /^( *)("[\w]+": )?("[^"]*"|[\w.+-]*)?([,[{])?$/mg;
        return JSON.stringify(obj, null, 3)
            .replace(/&/g, '&amp;').replace(/\\"/g, '&quot;')
            .replace(/</g, '&lt;').replace(/>/g, '&gt;')
            .replace(jsonLine, library.json.replacer);
    }
};

function getProfile() {
    $.ajax({
        type: "GET",
        url: "/getProfile",
        success: function (msg) {
            $('#data').html(library.json.prettyPrint(msg)).parent().show();
        },
        error: function () {
            alert("Access denied");
        }
    });
}

function getOrders() {
    $.ajax({
        type: "GET",
        url: "/getOrders",
        success: function (msg) {
            $('#data').html(library.json.prettyPrint(msg)).parent().show();
        },
        error: function () {
            alert("Access denied");
        }
    });
}