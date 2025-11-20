/** @type {import('next').NextConfig} */
const nextConfig = {
  async rewrites() {
    return [
      {
        source: "/:path*",
        destination: "http://localhost:9081/:path*",
      },
    ];
  },
  reactCompiler: true,
};

export default nextConfig;
