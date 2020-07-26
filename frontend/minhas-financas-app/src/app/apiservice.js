import axios from 'axios'

const httpClient = axios.create({
    baseURL: 'http://localhost:8080'
})

class ApiService {

    constructor(apiUrl){
        this.apiUrl = apiUrl;
    }  

    get(url,objeto){
        const requestURL = `${this.apiUrl}${url}`
        return httpClient.get(requestURL,objeto);
    }

    post(url,objeto){
        const requestURL = `${this.apiUrl}${url}`;
        return httpClient.post(requestURL,objeto);
    }

     put(url,objeto){
        const requestURL = `${this.apiUrl}${url}`
        return httpClient.put(requestURL,objeto);
    }

    delete(url){
        const requestURL = `${this.apiUrl}${url}`
        return httpClient.delete(requestURL);
    }
}

export default ApiService