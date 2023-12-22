import requests

def make_api_request(input_param):
    url = 'http://localhost:8080/api/chat/AI'
    
    # Parameters to be sent with the GET request
    params = {'input': input_param}

    try:
        # Making the API request
        response = requests.post(url, params=params)

        # Check if the request was successful (status code 200)
        if response.status_code == 200:
            # Print the raw response text
            print("API Request Successful")
            print("Response:")
            print(response.text)
        else:
            print(f"API Request Failed with Status Code: {response.status_code}")
            print("Response:")
            print(response.text)

    except requests.RequestException as e:
        print(f"API Request Exception: {e}")

if __name__ == "__main__":
    # Example: Make an API request with input parameter "Hello"
    make_api_request("Hello")
