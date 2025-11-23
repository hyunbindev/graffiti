export function InviteCodeKakaoShare({groupName,code}){
    const {Kakao, location} = window;
    Kakao.Share.sendDefault({
        objectType: 'feed',
        content: {
            title: `${groupName} 초대장`,
            description: `${groupName}로 당신을 초대 합니다!!`,
            imageUrl: ``,
            link: {
                mobileWebUrl: `${process.env.NEXT_PUBLIC_API_URL}/code/${code}`,
            }
        }
    });
};