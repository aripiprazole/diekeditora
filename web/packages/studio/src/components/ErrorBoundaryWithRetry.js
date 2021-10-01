import React from 'react';

export default class ErrorBoundaryWithRetry extends React.Component {
  state = {error: null};

  static getDerivedStateFromError(error) {
    return {error};
  }

  _retry() {
    // This ends up calling loadQuery again to get and render
    // a new query reference
    this.props.onRetry();
    this.setState({
      // Clear the error
      error: null,
    });
  }

  render() {
    const {children, fallback} = this.props;
    const {error} = this.state;
    if (error) {
      if (typeof fallback === 'function') {
        return fallback({error, retry: this._retry});
      }
      return fallback;
    }

    return children;
  }
}
