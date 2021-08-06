/**
 * Throws a not implemented error
 * 
 * @param message 
 */
function todo<T>(message: string = ""): T {
  throw Error("Not implemented: " + message);
}

export default todo;