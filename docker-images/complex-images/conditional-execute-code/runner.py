import sys

def main():
    if len(sys.argv) != 4:
        print("Usage: python runner.py <input_file> <output_file> <code_string>")
        sys.exit(1)

    input_file = sys.argv[1]
    output_file = sys.argv[2]
    code_string = sys.argv[3]

    # Read content from the input file
    with open(input_file, 'r') as f:
        file = f.read()

    # Dictionary to store local variables
    local_vars = {'file': file, 'result': None}

    # Execute the code string in the context of local_vars
    exec(code_string, {}, local_vars)

    # Expected result in['result']
    result = local_vars.get('result', '')

    # Save the result to the output file
    with open(output_file, 'w') as f:
        f.write(str(result))

if __name__ == "__main__":
    main()
