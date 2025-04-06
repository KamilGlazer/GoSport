import Logo from './Logo'

const LoadingBar = () => {
  return (
    <div className="loading-container">
            <Logo />
      <div className="loading-bar">
        <div className="loading-progress"></div>
      </div>
    </div>
  );
};

export default LoadingBar;