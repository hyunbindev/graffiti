/** @type {import('next').NextConfig} */
const nextConfig = {
  async rewrites() {
    return [
      {
        source: "/api/:path*",
        destination: "http://localhost:9081/:path*",
      },
    ];
  },
  reactCompiler: true,
};

export default nextConfig;
