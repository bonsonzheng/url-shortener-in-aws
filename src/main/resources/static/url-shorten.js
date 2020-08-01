function post_url() {

    let long_url = document.getElementById("url").value
    let result = document.getElementById("result")
    let url = "/url-map";

    fetch(url, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: data
    })
        .then(
            response => response.json()
        ).then(
        responseBody => result.innerHTML = '<a href = "' + responseBody["shortUrl"] + '" + >' + responseBody["shortUrl"] + '</a>'
    );
}